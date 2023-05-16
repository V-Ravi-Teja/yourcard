package com.ness.transactionservice.service;

import com.ness.transactionservice.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {
    public Integer addTransaction(TransactionDTO transactionDto);

    public List<TransactionDTO> getallTransaction(Integer userid);

    public void updateTransactionDetails(Integer TransactionId, TransactionDTO transactionDTO);

    public void deleteTransaction(Integer TransactionId);
    boolean checkIfTransactionPresent(Integer TransactionId);

    List<TransactionDTO> getTxByCategory(List<TransactionDTO> T, String category);
}