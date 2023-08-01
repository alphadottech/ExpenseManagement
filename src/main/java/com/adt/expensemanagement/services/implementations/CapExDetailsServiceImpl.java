package com.adt.expensemanagement.services.implementations;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.adt.expensemanagement.models.CapExDetails;
import com.adt.expensemanagement.repositories.CapExDetailsRepository;
import com.adt.expensemanagement.services.interfaces.CapExDetailsService;
@Service
public class CapExDetailsServiceImpl implements CapExDetailsService {
	
	@Autowired
	private CapExDetailsRepository capExDetailsRepository;

	//HRMS-114 -> START
	@Override
	public CapExDetails createCapExDetails(MultipartFile invoice,CapExDetails capExDetails) {
            try {
            	capExDetails.setInvoice(invoice.getBytes());
            	return capExDetailsRepository.save(capExDetails);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
	}
	//HRMS-114 -> END
	//HRMS-107 -> START
	@Override
    public List<CapExDetails> getAllCapExDetails() {
        return capExDetailsRepository.findAll();
    }
    @Override
    public CapExDetails getCapExDetailsById(int id) {
        return capExDetailsRepository.findById(id).orElse(null);
    }
  
  //HRMS-114 -> START
  @Override
  public String  updateCapExDetailsById(MultipartFile invoice, CapExDetails capExDetails) {
	  int id = capExDetails.getId();
	  CapExDetails capEx = capExDetailsRepository.findById(id).orElse(null);
      if (capEx != null) {
    	  capEx.setDate(capExDetails.getDate());
    	  capEx.setExpenseDetails(capExDetails.getExpenseDetails());;
    	  capEx.setGstBill(capExDetails.getGstBill());
    	  capEx.setAmount(capExDetails.getAmount());
    	  capEx.setPaidBy(capExDetails.getPaidBy());
    	  capEx.setComment(capExDetails.getComment());
    	  capEx.setMode(capExDetails.getMode());
          if (invoice != null && !invoice.isEmpty()) {
              try {
            	  capEx.setInvoice(invoice.getBytes());
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
          return " Capital Expenses Updated Successfully for ID : "+capExDetailsRepository.save(capEx).getId() ;
      } else {
          return "Update Operation Faild";
      }
  }
//HRMS-114 -> END

  @Override
    public boolean deleteCapExDetailsById(int id) {
        Optional<CapExDetails> capExDetailsOptional = capExDetailsRepository.findById(id)    ;
        if (capExDetailsOptional.isPresent()) {
            capExDetailsRepository.deleteById(id)    ;
            return true;
        }
        return false;
    }
	//HRMS-107 -> END

}
