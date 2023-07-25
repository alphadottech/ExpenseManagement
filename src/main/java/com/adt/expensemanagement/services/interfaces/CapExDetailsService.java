package com.adt.expensemanagement.services.interfaces;

import java.util.List;

import com.adt.expensemanagement.models.CapExDetails;

public interface CapExDetailsService  {
	
	    public CapExDetails createCapExDetails(CapExDetails capExDetails);
	  
	  	List<CapExDetails> getAllCapExDetails();

	    CapExDetails getCapExDetailsById(int id);

	    CapExDetails updateCapExDetailsById(int id, CapExDetails capExDetails);

	    boolean deleteCapExDetailsById(int id);
}
