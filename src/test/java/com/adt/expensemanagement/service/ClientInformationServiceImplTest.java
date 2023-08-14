package com.adt.expensemanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adt.expensemanagement.models.ClientInformation;
import com.adt.expensemanagement.models.ResponseModel;
import com.adt.expensemanagement.repositories.ClientInformationRepo;
import com.adt.expensemanagement.services.implementations.ClientInformationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ClientInformationServiceImplTest {

	@Mock
	private ClientInformationRepo clientInformationRepo;

	@InjectMocks
	private ClientInformationServiceImpl clientInformationServiceImpl;

	@DisplayName("JUnit test for saveClientInfo method")
	@Test
	public void saveClientInfo() {
		ClientInformation clientInfo = ClientInformation.builder().id(10).address("Indore").companyName("Alphadot Tech")
				.contactPerson("me").email("jd.adt@gmail.com").gstin("GHJY!23").build();

		when(clientInformationRepo.save(clientInfo)).thenReturn(clientInfo);
		ResponseModel responseModel = new ResponseModel();
		responseModel.setMsg("Success");
		assertEquals(responseModel, clientInformationServiceImpl.saveClientInfo(clientInfo));
	}

	@DisplayName("JUnit test for getAllClientInfo method")
	@Test
	public void getAllClientInfo() {
		ClientInformation clientInfo = ClientInformation.builder().id(10).address("Indore").companyName("Alphadot Tech")
				.contactPerson("me").email("jd.adt@gmail.com").gstin("GHJY!23").build();
		List<ClientInformation> clientInfoList = Collections.singletonList(clientInfo);
		when(clientInformationRepo.findAll()).thenReturn(clientInfoList);
		assertEquals(clientInfoList, clientInformationServiceImpl.getAllClientInfo());
	}

	@DisplayName("JUnit test for updateClientInfo method")
	@Test
	public void updateClientInfo() {
		ClientInformation clientInfo = ClientInformation.builder().id(10).address("Indore").companyName("Alphadot Tech")
				.contactPerson("me").email("jd.adt@gmail.com").gstin("GHJY!23").build();
		when(clientInformationRepo.findById(clientInfo.getId())).thenReturn(Optional.of(clientInfo));
		when(clientInformationRepo.save(clientInfo)).thenReturn(clientInfo);
		assertEquals("update successfully",
				clientInformationServiceImpl.updateClientInfo(clientInfo.getId(), clientInfo));

	}

	@DisplayName("JUnit test for getClientInfoById method")
	@Test
	public void getClientInfoById() {
		ClientInformation clientInfo = ClientInformation.builder().id(10).address("Indore").companyName("Alphadot Tech")
				.contactPerson("me").email("jd.adt@gmail.com").gstin("GHJY!23").build();
//		 List<ClientInformation> clientInfoList = Collections.singletonList(clientInfo);

		when(clientInformationRepo.findById(clientInfo.getId())).thenReturn(Optional.of(clientInfo));
		assertEquals(clientInfo, clientInformationServiceImpl.getClientInfoById(clientInfo.getId()));
	}

	@DisplayName("JUnit test for SearchByCompany method")
	@Test
	public void SearchByCompany() {
		ClientInformation clientInfo = ClientInformation.builder().id(10).address("Indore").companyName("Alphadot Tech")
				.contactPerson("me").email("jd.adt@gmail.com").gstin("GHJY!23").build();
		List<ClientInformation> clientInfoList = Collections.singletonList(clientInfo);
		when(clientInformationRepo.findByCompanyName(clientInfo.getCompanyName())).thenReturn(clientInfoList);
		assertEquals(clientInfoList, clientInformationServiceImpl.SearchByCompany(clientInfo.getCompanyName()));
	}

	@DisplayName("JUnit test for SearchByEmail method")
	public void SearchByEmail() {
		ClientInformation clientInfo = ClientInformation.builder().id(10).address("Indore").companyName("Alphadot Tech")
				.contactPerson("me").email("jd.adt@gmail.com").gstin("GHJY!23").build();
		List<ClientInformation> clientInfoList = Collections.singletonList(clientInfo);
		when(clientInformationRepo.findByEmail(clientInfo.getEmail())).thenReturn(clientInfoList);
		assertEquals(clientInfoList, clientInformationServiceImpl.SearchByEmail(clientInfo.getEmail()));
	}

}
