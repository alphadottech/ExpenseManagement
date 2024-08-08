package com.adt.expensemanagement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.adt.expensemanagement.models.ClientInformation;
@Repository
public interface ClientInformationRepo extends JpaRepository<ClientInformation, Integer>{
	
	//--------------- ** Jira No. HRMS-84 START **---------------------

	@Query(value= "FROM ClientInformation ci WHERE  ci.companyName like %:query%")
	List<ClientInformation> findByCompanyName(@Param("query") String companyName);
	
	@Query(value= "FROM ClientInformation ci WHERE ci.email like %:query%")
	List<ClientInformation> findByEmail(@Param("query") String email);

    List<ClientInformation> findByEmailAndCompanyName(String email, String companyName);

    //--------------- ** Jira No. HRMS-84 END **---------------------


}
