package com.adt.expensemanagement.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adt.expensemanagement.models.ClientInformation;
import com.adt.expensemanagement.models.ResponseModel;
import com.adt.expensemanagement.services.interfaces.ClientInformationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/clientInfo")
public class ClientInformationController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ClientInformationService informationService;

	@PreAuthorize("@auth.allow('SAVE_CLIENT_INFO')")
	@PostMapping("/saveClientInfo")
	public ResponseEntity<ResponseModel> saveClientInfo(@RequestBody ClientInformation clientInfo) {
		LOGGER.info("Expense Management: clientInfo : saveClientInfo Info level log message");

		ResponseModel model = informationService.saveClientInfo(clientInfo);
		return new ResponseEntity<>(model, HttpStatus.OK);
	}

	@PreAuthorize("@auth.allow('GET_ALL_CLIENT_INFO')")
	@GetMapping("/getAllClientInfo")
	public ResponseEntity<List<ClientInformation>> getAllClientInfo() {
		LOGGER.info("Expense Management: getAllClientInfo : getAllClientInfo Info level log message");

		List<ClientInformation> information = informationService.getAllClientInfo();
		return new ResponseEntity<>(information, HttpStatus.OK);
	}

	@PreAuthorize("@auth.allow('UPDATE_CLIENT_INFO')")
	@PutMapping("/updateClientInfo/{id}")
	public ResponseEntity<String> updateClientInfo(@PathVariable("id") int id,
			@RequestBody ClientInformation clientInformation, HttpServletRequest request) {
		LOGGER.info("API Call From IP: " + request.getRemoteHost());
		return new ResponseEntity<>(informationService.updateClientInfo(id, clientInformation), HttpStatus.OK);
	}

	@PreAuthorize("@auth.allow('GET_CLIENT_INFO_BY_ID')")
	@GetMapping("/getClientInfoById/{clientId}")
	public ResponseEntity<ClientInformation> getClientInformationById(@PathVariable("clientId") int clientId) {
		LOGGER.info("Expense Management: ClientInformationController: getClientInformationById info level log message");
		ClientInformation clientInfo = informationService.getClientInfoById(clientId);
		if (clientInfo == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(clientInfo);
	}

	@PreAuthorize("@auth.allow('SEARCH_BY_EMAIL_AND_COMPANY_NAME')")
	@GetMapping("/searchByEmailAndCompanyName")
	public ResponseEntity<List<ClientInformation>> searchByEmailAndCompanyName(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "companyName", required = false) String companyName) {

		LOGGER.info("Expense Management: searchByEmailAndCompanyName Info level log message");

		return ResponseEntity.ok(informationService.searchByEmailAndCompanyName(email, companyName));
	}

}


