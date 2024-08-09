package com.adt.expensemanagement.services.implementations;

import com.adt.expensemanagement.config.Auth;
import com.adt.expensemanagement.models.ExpenseItems;
import com.adt.expensemanagement.models.Mail;
import com.adt.expensemanagement.models.OnExpenseRequestSaveEvent;
import com.adt.expensemanagement.models.User;
import com.adt.expensemanagement.repositories.UserRepo;
import com.adt.expensemanagement.services.interfaces.CommonEmailService;
import com.adt.expensemanagement.util.TableDataExtractor;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmailService implements CommonEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Configuration templateConfiguration;

    @Value("${app.velocity.templates.location}")
    private String basePackagePath;

    @Autowired
    private TableDataExtractor dataExtractor;

    @Autowired
    private Auth auth;

    @Value("${email.service.url}")
    private String emailServiceUrl;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RestTemplate restTemplate;



    @Override
    public void sendEmail(OnExpenseRequestSaveEvent event, String approveUrl, String rejectUrl, ExpenseItems expenseItems) throws TemplateException, IOException, MessagingException {
        Mail mail = new Mail();
        mail.setSubject("Expense Approval Request");

        //*** From whom the mail should come ***
        Integer empID = expenseItems.getEmpId();
        Optional<User> user = userRepo.findById(empID);
        String userEmail = user.get().getEmail();
        String employeeName=user.get().getFirstName()+" "+user.get().getLastName();
        mail.setFrom(userEmail);

        //*** Get recipient email and generate token ***
        String sql = "SELECT email_id FROM av_schema.priortime_email where designation='CEO'";
        List<Map<String, Object>> ExpenseData = dataExtractor.extractDataFromTable(sql);
        for (Map<String, Object> expenses : ExpenseData) {
           String email = String.valueOf(expenses.get("email_id"));
            String token = auth.tokenGanreate(email);
            mail.setTo(email);
            mail.getModel().put("approvalUrl", approveUrl + "?Authorization=" + token);
            mail.getModel().put("rejectionUrl", rejectUrl + "?Authorization=" + token);
            mail.getModel().put("EmployeeName",employeeName);
            mail.getModel().put("ExpenseAmount", String.valueOf(event.getExpenseItems().getAmount()));
            mail.getModel().put("ExpenseDate", String.valueOf(event.getExpenseItems().getPaymentDate()));
            mail.getModel().put("ExpensePurpose",event.getExpenseItems().getDescription().toString());

            templateConfiguration.setClassForTemplateLoading(getClass(), basePackagePath);
            Template template = templateConfiguration.getTemplate("expense_status_approval.ftl");
            String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
            mail.setContent(mailContent);
            String url = emailServiceUrl + "/emails/send";
            HttpEntity<Mail> request = new HttpEntity<>(mail);
            restTemplate.postForEntity(url, request, String.class);
          // send(mail);
        }
    }

    @Override
    public void sendEmail(OnExpenseRequestSaveEvent event) {
        ExpenseItems expenseItems = event.getExpenseItems();
        String emailApprovalUrl = event.getApproveUrlBuilder().toUriString();
        String emailRejectionUrl = event.getRejectUrlBuilder().toUriString();

        try {
            sendEmail(event, emailApprovalUrl, emailRejectionUrl, expenseItems);
        } catch (IOException | TemplateException | MessagingException e) {
            e.printStackTrace();
        }
    }

    private void send(Mail mail) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

        helper.setTo(mail.getTo())  ;
        helper.setText(mail.getContent(), true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());
        mailSender.send(mimeMessage);

    }
}
