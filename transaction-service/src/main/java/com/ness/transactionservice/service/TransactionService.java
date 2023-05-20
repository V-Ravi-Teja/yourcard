package com.ness.transactionservice.service;

import com.ness.transactionservice.dto.TransactionDTO;
import com.ness.transactionservice.exception.TransactionNotFound;

import java.util.List;

public interface TransactionService {
    public Integer addTransaction(TransactionDTO transactionDto);

    public List<TransactionDTO> getAllTransaction(Integer userid);

    TransactionDTO getTransaction(Integer transactionId) throws TransactionNotFound;

    List<TransactionDTO> getTxByCategory(List<TransactionDTO> T, String category);

    public void updateTransactionDetails(Integer TransactionId, TransactionDTO transactionDTO) throws TransactionNotFound;

    public void deleteTransaction(Integer TransactionId) throws TransactionNotFound;
    public Integer  getCardBalanceLeft(int userId);


    public boolean doesUserExist(int userId);
}