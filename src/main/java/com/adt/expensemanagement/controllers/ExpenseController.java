package com.adt.expensemanagement.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;


import com.adt.expensemanagement.models.OnExpenseRequestSaveEvent;
import com.adt.expensemanagement.repositories.ExpenseRepository;
import com.adt.expensemanagement.services.implementations.EmailService;
import com.adt.expensemanagement.models.ApiResponse;
import com.adt.expensemanagement.utilities.errorResponseUtilities.ApiError;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adt.expensemanagement.models.ExpenseItems;
import com.adt.expensemanagement.models.ExpenseOutbound;
import com.adt.expensemanagement.services.interfaces.ExpenseService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class ExpenseController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ExpenseService expenseService;

	@Autowired
	private ExpenseRepository expenseRepository;

	@Autowired
	private Configuration freemarkerConfig;

	@Autowired
	private EmailService emailService;

	@Value("${app.velocity.templates.location}")
	private String basePackagePath;

	@Autowired
	ApplicationEventPublisher applicationEventPublisher;

	@Value("${-Dmy.port}")
	private String serverPort;

	@Value("${-Dmy.property}")
	private String ipAddress;

	@Value("${-UI.scheme}")
	private String scheme;

	@Value("${-UI.context}")
	private String context;

	@PreAuthorize("@auth.allow('GET_ALL_EXPENSES')")
	@GetMapping("/getAllExpenses")
	public ResponseEntity<List<ExpenseItems>> getAllExpenses(HttpServletRequest request) {
		LOGGER.info("API Call From IP: " + request.getRemoteHost());
		List<ExpenseItems> expenses = expenseService.getAllExpenses();
		return new ResponseEntity<>(expenses, HttpStatus.OK);
	}

	@PreAuthorize("@auth.allow('CREATE_EXPENSES')")
	@PostMapping("/createExpenses")
	public ResponseEntity<ApiResponse> createExpenses(@RequestBody @Valid ExpenseItems expenseItems,
													  HttpServletRequest request) {

		LOGGER.info("API Call From IP: " + request.getRemoteHost());
		try {
			expenseItems.setStatus("Pending");
			ExpenseItems savedExpense = expenseService.createExpenses(expenseItems);

			int expenseId = savedExpense.getId();
			UriComponentsBuilder approveUrlBuilder = ServletUriComponentsBuilder.newInstance()
					.scheme(scheme)
					.host(ipAddress)
					.port(serverPort)
					.path(context + "/expensemanagement/approveOrRejectExpense/" + expenseId + "/approved");

			UriComponentsBuilder rejectUrlBuilder = ServletUriComponentsBuilder.newInstance()
					.scheme(scheme)
					.host(ipAddress)
					.port(serverPort)
					.path(context + "/expensemanagement/approveOrRejectExpense/" + expenseId + "/rejected");

			OnExpenseRequestSaveEvent onExpenseCreatedEvent = new OnExpenseRequestSaveEvent(approveUrlBuilder, rejectUrlBuilder, savedExpense);
			applicationEventPublisher.publishEvent(onExpenseCreatedEvent);

			return ResponseEntity.ok(new ApiResponse(true, "Expense is created and Mail sent successfully."));
		} catch (Exception e) {
			LOGGER.error("Error creating expense: " + e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PreAuthorize("@auth.allow('APPROVE_REJECT_EXPENSES')")
	@GetMapping("/approveOrRejectExpense/{id}/{action}")
	public ResponseEntity<String> approveOrRejectExpense(@PathVariable int id, @PathVariable String action, HttpServletRequest request)
			throws IOException, TemplateException {
		LOGGER.info("API Call From IP: " + request.getRemoteHost());
		try {
			String message;
			String currentStatus = expenseService.getExpenseStatus(id);

			if ("approved".equalsIgnoreCase(action)) {
				if ("approved".equalsIgnoreCase(currentStatus)) {
					message = "Expense is already approved!";
				} else if ("rejected".equalsIgnoreCase(currentStatus)) {
					message = "Cannot approve an expense that is already rejected!";
				} else {
					expenseService.updateExpenseStatus(id, "approved");
					message = "Expense approved successfully!";
				}
			} else if ("rejected".equalsIgnoreCase(action)) {
				if ("rejected".equalsIgnoreCase(currentStatus)) {
					message = "Expense is already rejected!";
				} else if ("approved".equalsIgnoreCase(currentStatus)) {
					message = "Cannot reject an expense that is already approved!";
				} else {
					expenseService.updateExpenseStatus(id, "rejected");
					message = "Expense rejected!";
				}
			} else {
				return new ResponseEntity<>("Invalid action", HttpStatus.BAD_REQUEST);
			}

			freemarkerConfig.setClassForTemplateLoading(getClass(), basePackagePath);
			Template template = freemarkerConfig.getTemplate("message.ftl");
			Map<String, Object> model = new HashMap<>();
			model.put("Message", message);

			String processedTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
			return new ResponseEntity<>(processedTemplate, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Error occurred while processing expense action: ", e);
			return new ResponseEntity<>("An error occurred while processing the request.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PreAuthorize("@auth.allow('UPDATE_EXPENSE')")
	@PutMapping("/updateExpense/{id}")
	public ResponseEntity<String> updateExpense(@PathVariable("id") int id, @RequestBody ExpenseItems expenseItems,
			HttpServletRequest request) {
		LOGGER.info("API Call From IP: " + request.getRemoteHost());
		return new ResponseEntity<>(this.expenseService.updateExpense(id, expenseItems), HttpStatus.OK);
	}

	
	@PreAuthorize("@auth.allow('GET_EXPENSE_BY_ID')")
	@GetMapping("/getExpenseById/{id}")
	public ResponseEntity<ExpenseItems> getExpenseById(@PathVariable("id") int id, HttpServletRequest request)
			throws NoSuchFieldException {
		LOGGER.info("API Call From IP: " + request.getRemoteHost());
		return new ResponseEntity<>(expenseService.getExpenseById(id), HttpStatus.OK);
	}

	@PreAuthorize("@auth.allow('SAVE_OUTBOUND_EXPENSE')")
	@PostMapping("/saveOutboundExpense")
	public ResponseEntity<String> saveOutboundExpense(@RequestBody ExpenseOutbound expenseOutbound,
			HttpServletRequest request) {
		LOGGER.info("API Call From IP: " + request.getRemoteHost());
		return new ResponseEntity<>(expenseService.saveOutboundExpense(expenseOutbound), HttpStatus.OK);
	}

	@PreAuthorize("@auth.allow('UPDATE_OUTBOUND_EXPENSE')")
	@PutMapping("updateOutboundExpense")
	public ResponseEntity<String> updateOutboundExpense(@RequestBody ExpenseOutbound expenseOutbound,
			HttpServletRequest request) {
		LOGGER.info("API Call From IP: " + request.getRemoteHost());
		return new ResponseEntity<>(expenseService.updateOutboundExpense(expenseOutbound), HttpStatus.OK);
	}

	@PreAuthorize("@auth.allow('GET_EXPENSE_BY_DATE_RANGE')")
	@GetMapping("/getExpenseByDateRange")
	public ResponseEntity<List<ExpenseItems>> getExpenseByDateRange(@RequestParam("startDate") String from,
			@RequestParam("endDate") String to, HttpServletRequest request)
			throws NoSuchFieldException, ParseException {
		LOGGER.info("API Call From IP: " + request.getRemoteHost());
		return new ResponseEntity<>(expenseService.getExpenseByDateRange(from, to), HttpStatus.OK);
	}

	@PreAuthorize("@auth.allow('DELETE_ALL_EXPENSE_BY_ID')")
	@DeleteMapping("/deleteAllExpenseById")
	public ResponseEntity<String> deleteAllExpenseByIds(@RequestBody List<Integer> ids, HttpServletRequest request)
			throws NoSuchFieldException {
		LOGGER.info("API Call From IP: " + request.getRemoteHost());
		return new ResponseEntity<>(expenseService.deleteAllExpenseByIds(ids), HttpStatus.OK);
	}

	@PreAuthorize("@auth.allow('DELETE_EXPENSE_BY_ID')")
	@DeleteMapping("/deleteExpenseById/{id}")
	public ResponseEntity<String> deleteExpenseById(@PathVariable("id") int id, HttpServletRequest request)
			throws NoSuchFieldException {
		LOGGER.info("API Call From IP: " + request.getRemoteHost());
		return new ResponseEntity<>(expenseService.deleteExpenseById(id), HttpStatus.OK);
	}

	}
