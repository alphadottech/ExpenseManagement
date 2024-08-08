package com.adt.expensemanagement.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.adt.expensemanagement.models.CapExDetails;
import com.adt.expensemanagement.services.interfaces.CapExDetailsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/capExDetails")
public class CapExDetailsController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CapExDetailsService capExDetailsService;

	@PreAuthorize("@auth.allow('CREATE_CAPITAL_EXPENSE_DETAILS')")
	@PostMapping("/createCapExDetails")
	public ResponseEntity<String> createCapExDetails(@RequestPart("invoice") MultipartFile invoice,
			@RequestPart String body, HttpServletRequest request) throws JsonMappingException, JsonProcessingException {
		LOGGER.info("Expenseservice:capExDetails:createCapExDetails info level log message");
		ObjectMapper mapper = new ObjectMapper();
		CapExDetails capExDetails = mapper.readValue(body, CapExDetails.class);
		CapExDetails details = capExDetailsService.createCapExDetails(invoice, capExDetails);

		if (body != null) {
			return new ResponseEntity<>("Capital Expenses saved Successfully for ID : " + details.getId(),
					HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PreAuthorize("@auth.allow('GET_ALL_CAPITAL_EXPENSE_DETAILS')")
	@GetMapping("/getAllCapExDetails")
	public ResponseEntity<List<Map<String, Object>>> getAllCapExDetails() {
		LOGGER.info("Expenseservice: capExDetails:getAllCapExDetails info level log message");
		List<Map<String, Object>> capExDetailsList = capExDetailsService.getAllCapExDetails();
		return new ResponseEntity<>(capExDetailsList, HttpStatus.OK);
	}

	@PreAuthorize("@auth.allow('GET_CAPITAL_EXPENSE_DETAILS_BY_ID')")
	@GetMapping("/getByCapExDetails/{id}")
	public ResponseEntity<CapExDetails> getCapExDetailsById(@PathVariable int id) {
		LOGGER.info("Expenseservice:capExDetails:getCapExDetailsById info level log message");
		CapExDetails capExDetails = capExDetailsService.getCapExDetailsById(id);
		if (capExDetails != null) {
			return new ResponseEntity<>(capExDetails, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PreAuthorize("@auth.allow('UPDATE_CAPITAL_EXPENSE_DETAILS_BY_ID')")
	@PutMapping(value = "/UpdateCapExDetails")
	public ResponseEntity<String> updateCapExDetails(@RequestPart("invoice") MultipartFile invoice,
			@RequestPart String details) throws JsonMappingException, JsonProcessingException {
		LOGGER.info("Expenseservice:capExDetails:updateCapExDetailsById info level log message");
		ObjectMapper mapper = new ObjectMapper();
		CapExDetails capExDetails = mapper.readValue(details, CapExDetails.class);
		String updatedCapExDetails = capExDetailsService.updateCapExDetailsById(invoice, capExDetails);
		if (updatedCapExDetails != null) {
			return new ResponseEntity<>(updatedCapExDetails, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PreAuthorize("@auth.allow('DELETE_CAPITAL_EXPENSE_DETAILS_BY_ID')")
	@DeleteMapping("deleteCapExById/{id}")
	public ResponseEntity<String> deleteCapExDetailsById(@PathVariable int id) {
		LOGGER.info("Expenseservice:capExDetails:deleteCapExDetailsById info level log message");
		boolean isDeleted = capExDetailsService.deleteCapExDetailsById(id);
		if (isDeleted) {
			String successMessage = "Data with ID " + id + " deleted successfully";
			return new ResponseEntity<>(successMessage, HttpStatus.OK);
		} else {
			String errorMessage = "Data with ID " + id + " not found";
			return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
		}
	}

	@PreAuthorize("@auth.allow('SEARCH__CAPITAL_EXPENSE_DETAILS_BY_DATE')")
	@GetMapping("/searchCapExDetailsByDate")
	public ResponseEntity<Object> searchCapExDetailsByDate(
			@RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
			@RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
			HttpServletRequest request) {
		LOGGER.info("Expenseservice: capExDetails:searchCapExDetailsByDate info level log message");
		try {
			List<Map<String, Object>> capExDetailsList = capExDetailsService.searchCapExDetailsByDate(fromDate, toDate);
			return new ResponseEntity<>(capExDetailsList, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Error occurred: ", e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}