package com.adt.expensemanagement.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.adt.expensemanagement.util.CapExDetailsUtility;
import com.adt.expensemanagement.util.GSTInvoiceUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.adt.expensemanagement.models.ClientInformation;
import com.adt.expensemanagement.models.ResponseModel;
import com.adt.expensemanagement.repositories.ClientInformationRepo;
import com.adt.expensemanagement.services.interfaces.ClientInformationService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientInformationServiceImpl implements ClientInformationService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ClientInformationRepo informationRepo;

	@Autowired
	private MessageSource messageSource;

	@Override
	public ResponseModel saveClientInfo(ClientInformation clientInfo) {
		if(!CapExDetailsUtility.validateCompany(clientInfo.getCompanyName())){
			throw new IllegalArgumentException("Invalid Company Name");
		}
		if(!CapExDetailsUtility.validateGST(clientInfo.getGstin())){
			throw new IllegalArgumentException("Invalid GST Details");
		}
		if(!GSTInvoiceUtility.validateString(clientInfo.getContactPerson())){
			throw new IllegalArgumentException("Invalid Contact Person Details");
		}
		if(!GSTInvoiceUtility.checkValidate(clientInfo.getAddress())){
			throw new IllegalArgumentException("Invalid Address");
		}
		if(!GSTInvoiceUtility.validateEmail(clientInfo.getEmail())){
			throw new IllegalArgumentException("Invalid Email Format");
		}
		if(!GSTInvoiceUtility.validatePhoneNo(clientInfo.getPhone())){
			throw new IllegalArgumentException("Invalid Phone Number");
		}
		informationRepo.save(clientInfo);
		ResponseModel responseModel = new ResponseModel();
		responseModel.setMsg("Success");
		return responseModel;
	}

	@Override
	public List<ClientInformation> getAllClientInfo() {
		List<ClientInformation> list = informationRepo.findAll();
		return list;
	}

	@Override
	public String updateClientInfo(int id, ClientInformation clientInformation) {
		Optional<ClientInformation> clientInfo = informationRepo.findById(id);
		if (!clientInfo.isPresent()) {
			String message = messageSource.getMessage("api.error.data.not.found.id", null, Locale.ENGLISH);
			LOGGER.error(message = message + id);
			throw new EntityNotFoundException(message);
		}
		clientInfo.get().setAddress(clientInformation.getAddress());
		clientInfo.get().setCompanyName(clientInformation.getCompanyName());
		clientInfo.get().setContactPerson(clientInformation.getContactPerson());
		clientInfo.get().setEmail(clientInformation.getEmail());
		clientInfo.get().setGstin(clientInformation.getGstin());
		clientInfo.get().setPhone(clientInformation.getPhone());
		informationRepo.save(clientInfo.get());
		return "update successfully";
	}

	@Override
	public ClientInformation getClientInfoById(int clientId) {
		Optional<ClientInformation> opt = informationRepo.findById(clientId);
		if (!opt.isPresent())
			return null;

		return opt.get();
	}

	// -----------------** Jira No. HRMS-84 START **-------------


	//---------- ** Jira No. HRMS-84 END **-----------
	@Override
	public List<ClientInformation> searchByEmailAndCompanyName(String email, String companyName) {
		if (email != null && companyName != null) {
			return informationRepo.findByEmailAndCompanyName(email, companyName);
		} else if (email != null) {
			return informationRepo.findByEmail(email);
		} else if (companyName != null) {
			return informationRepo.findByCompanyName(companyName);
		} else {
			return new ArrayList<>();
		}
	}
}
