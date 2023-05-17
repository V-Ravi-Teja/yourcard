package com.ness.transactionservice.service;

import com.ness.transactionservice.dto.TransactionDTO;
import com.ness.transactionservice.exception.TransactionNotFound;

import java.util.List;

public interface TransactionService {
    public Integer addTransaction(TransactionDTO transactionDto);

    public List<TransactionDTO> getallTransaction(Integer userid);

    public void updateTransactionDetails(Integer TransactionId, TransactionDTO transactionDTO) throws TransactionNotFound;

    public void deleteTransaction(Integer TransactionId) throws TransactionNotFound;

    List<TransactionDTO> getTxByCategory(List<TransactionDTO> T, String category);
}