package com.adt.expensemanagement.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.adt.expensemanagement.models.CapExDetails;
import com.adt.expensemanagement.services.interfaces.CapExDetailsService;

@RestController
@RequestMapping("/capExDetails")
public class CapExDetailsController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CapExDetailsService capExDetailsService;

	@PreAuthorize("@auth.allow('ROLE_ADMIN')")
	@PostMapping("/createCapExDetails")
	public ResponseEntity<CapExDetails> createCapExDetails(@RequestBody CapExDetails capExDetails,
			HttpServletRequest request) {
		LOGGER.info("Expenseservice:capExDetails:createCapExDetails info level log message");
		CapExDetails details = capExDetailsService.createCapExDetails(capExDetails);
		return new ResponseEntity<>(details, HttpStatus.OK);
	}
	
	//HRMS-107 -> START
	@PreAuthorize("@auth.allow('ROLE_ADMIN')")
	@GetMapping("/")
	public ResponseEntity<List<CapExDetails>> getAllCapExDetails() {
		LOGGER.info("Expenseservice:capExDetails:getAllCapExDetails info level log message");
	    List<CapExDetails> capExDetailsList = capExDetailsService.getAllCapExDetails();
	    return new ResponseEntity<>(capExDetailsList, HttpStatus.OK);
	}

	@PreAuthorize("@auth.allow('ROLE_ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<CapExDetails> getCapExDetailsById(@PathVariable int id) {
		LOGGER.info("Expenseservice:capExDetails:getCapExDetailsById info level log message");
	    CapExDetails capExDetails = capExDetailsService.getCapExDetailsById(id);
	    if (capExDetails != null) {
	        return new ResponseEntity<>(capExDetails, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}

	@PreAuthorize("@auth.allow('ROLE_ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<CapExDetails> updateCapExDetailsById(@PathVariable int id, @RequestBody CapExDetails capExDetails)
	{
		LOGGER.info("Expenseservice:capExDetails:updateCapExDetailsById info level log message");
	    CapExDetails updatedCapExDetails = capExDetailsService.updateCapExDetailsById(id, capExDetails);
	    if (updatedCapExDetails != null) {
	        return new ResponseEntity<>(updatedCapExDetails, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}

	@PreAuthorize("@auth.allow('ROLE_ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCapExDetailsById(@PathVariable int id) {
		LOGGER.info("Expenseservice:capExDetails:deleteCapExDetailsById info level log message");
	    boolean isDeleted = capExDetailsService.deleteCapExDetailsById(id)	;
	    if (isDeleted) {
	        String successMessage = "Data with ID " + id + " deleted successfully";
	        return new ResponseEntity<>(successMessage, HttpStatus.OK);
	    } else {
	        String errorMessage = "Data with ID " + id + " not found";
	        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
	    }
	}
//	@DeleteMapping("/{id}")
//	public ResponseEntity<Void> deleteCapExDetailsById(@PathVariable int id) {
//	    capExDetailsService.deleteCapExDetailsById(id);
//	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//	}
	//HRMS-107 -> END
}