package com.ness.transactionservice;

import com.ness.transactionservice.controller.TransactionController;
import com.ness.transactionservice.model.Transaction;
import com.ness.transactionservice.repository.TransactionRepository;
import com.ness.transactionservice.service.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.ness.transactionservice.dto.TransactionDTO;
import com.ness.transactionservice.exception.TransactionNotFound;
import com.ness.transactionservice.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionServiceApplicationTests {

	@Test
	void contextLoads() {
	}


	@Mock
	private TransactionService transactionService;

	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private TransactionController transactionController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddTransaction() {
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setUserId(1);
		transactionDTO.setMerchant("Merchant");
		transactionDTO.setAmount(BigDecimal.valueOf(100.0));
		transactionDTO.setCategory("Category");

		when(transactionService.doesUserExist(transactionDTO.getUserId())).thenReturn(true);
		when(transactionService.addTransaction(transactionDTO)).thenReturn(1);

		String expectedResponse = "transaction created with id:1";
		String response = transactionController.addTransaction(transactionDTO);

		assertEquals(expectedResponse, response);

		verify(transactionService, times(1)).doesUserExist(transactionDTO.getUserId());
		verify(transactionService, times(1)).addTransaction(transactionDTO);
	}
	@Test
	void testGetAllTransaction() {
		int userId = 1;
		List<TransactionDTO> transactionDTOs = new ArrayList<>();
		// Add some transactionDTOs to the list

		when(transactionService.doesUserExist(userId)).thenReturn(true);
		when(transactionService.getAllTransaction(userId)).thenReturn(transactionDTOs);

		List<TransactionDTO> response = transactionController.getAllTransaction(userId);

		assertEquals(transactionDTOs, response);

		verify(transactionService, times(1)).doesUserExist(userId);
		verify(transactionService, times(1)).getAllTransaction(userId);
	}

	@Test
	void testGetTransaction() throws TransactionNotFound {
		int transactionId = 1;
		TransactionDTO transactionDTO = new TransactionDTO();
		// Set properties of the transactionDTO

		when(transactionService.getTransaction(transactionId)).thenReturn(transactionDTO);

		TransactionDTO response = transactionController.getTransaction(transactionId);

		assertEquals(transactionDTO, response);

		verify(transactionService, times(1)).getTransaction(transactionId);
	}

}
