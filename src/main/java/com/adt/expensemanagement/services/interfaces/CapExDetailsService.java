package com.adt.expensemanagement.services.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.adt.expensemanagement.models.CapExDetails;

public interface CapExDetailsService  {
	
		//HRMS-114 -> START
	    public CapExDetails createCapExDetails(MultipartFile invoice,CapExDetails capExDetails);
	    //HRMS-114 -> END
		
	    //HRMS-107 -> START
	  	List<CapExDetails> getAllCapExDetails();
	    CapExDetails getCapExDetailsById(int id);

	    //HRMS-114 -> START
	    String updateCapExDetailsById(MultipartFile invoice ,CapExDetails capExDetails);
	    //HRMS-114 -> END

	    boolean deleteCapExDetailsById(int id);
		//HRMS-107 -> END

}
