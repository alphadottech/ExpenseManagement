package com.adt.expensemanagement.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adt.expensemanagement.models.CapExDetails;
import com.adt.expensemanagement.repositories.CapExDetailsRepository;
import com.adt.expensemanagement.services.interfaces.CapExDetailsService;
@Service
public class CapExDetailsServiceImpl implements CapExDetailsService {
	
	@Autowired
	private CapExDetailsRepository capExDetailsRepository;

	@Override
	public CapExDetails createCapExDetails(CapExDetails capExDetails) {
		CapExDetails capExDetail=capExDetailsRepository.save(capExDetails);
		return capExDetail;
	}

	@Override
    public List<CapExDetails> getAllCapExDetails() {
        return capExDetailsRepository.findAll();
    }
    @Override
    public CapExDetails getCapExDetailsById(int id) {
        return capExDetailsRepository.findById(id).orElse(null);
    }

    @Override
    public CapExDetails updateCapExDetailsById(int id, CapExDetails capExDetails) {
        capExDetails.setId(id);
        return capExDetailsRepository.save(capExDetails);
    }
    @Override
    public boolean deleteCapExDetailsById(int id) {
        Optional<CapExDetails> capExDetailsOptional = capExDetailsRepository.findById(id)    ;
        if (capExDetailsOptional.isPresent()) {
            capExDetailsRepository.deleteById(id)    ;
            return true;
        }
        return false;
    }

//    @Override
//    public boolean deleteCapExDetailsById(int id) {
//        capExDetailsRepository.deleteById(id);
//    }

}
