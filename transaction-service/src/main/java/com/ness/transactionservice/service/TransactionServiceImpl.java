package com.ness.transactionservice.service;

import com.ness.transactionservice.dto.TransactionDTO;
import com.ness.transactionservice.exception.TransactionNotFound;
import com.ness.transactionservice.model.Transaction;
import com.ness.transactionservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    public TransactionRepository transactionRepository;

    @Override
    public Integer addTransaction(TransactionDTO transactionDto){

        Transaction transaction=new Transaction();
        transactionRepository.save(mapToModel(transaction,transactionDto));
        return transaction.getTransactionId();
    }

    @Override
    public List<TransactionDTO> getallTransaction(Integer userid){
        List<Transaction> transactions=transactionRepository.findAllByUserId(userid);
        List<TransactionDTO> transactionDTOs = transactions.stream().map(this::mapToDTOForList).toList();
        return transactionDTOs;
    }

    @Override
    public void updateTransactionDetails(Integer TransactionId, TransactionDTO transactionDTO) throws TransactionNotFound {
        Optional<Transaction> optional = transactionRepository.findById(TransactionId);
        Transaction transaction =optional.orElseThrow(() -> new TransactionNotFound("Service.TRANSACTION_NOT_FOUND"));
        transactionRepository.save(mapToModel(transaction,transactionDTO));
    }

    @Override
    public  void deleteTransaction(Integer TransactionId) throws TransactionNotFound{
        Optional<Transaction> optional = transactionRepository.findById(TransactionId);
        Transaction transaction = optional.orElseThrow(() -> new TransactionNotFound("Service.TRANSACTION_NOT_FOUND"));
        transactionRepository.delete(transaction);
    }

    @Override
    public List<TransactionDTO> getTxByCategory(List<TransactionDTO> T, String category) {
        List<TransactionDTO> txByCategory = T.stream().
                filter(s -> category.equals(s.getCategory())).
                collect(Collectors.toList());
        return txByCategory;
    }

    private Transaction mapToModel(Transaction transaction, TransactionDTO transactionDto){
        transaction.setTransactionId(transactionDto.getTransactionId());
        transaction.setUserId(transactionDto.getUserId());
        transaction.setMerchant(transactionDto.getMerchant());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDate(transactionDto.getDate());
        transaction.setCategory(transactionDto.getCategory());
        return transaction;
    }
    private TransactionDTO mapToDTO(Transaction transaction, TransactionDTO transactionDTO){
        transactionDTO.setTransactionId(transaction.getTransactionId());
        transactionDTO.setUserId(transaction.getUserId());
        transactionDTO.setMerchant(transaction.getMerchant());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setDate(transaction.getDate());
        transactionDTO.setCategory(transaction.getCategory());
        return transactionDTO;
    }
    private TransactionDTO mapToDTOForList(Transaction transaction){
        TransactionDTO transactionDTO = new TransactionDTO(
                transaction.getTransactionId(),
                transaction.getUserId(),
                transaction.getMerchant(),
                transaction.getAmount(),
                transaction.getDate(),
                transaction.getCategory());
        return transactionDTO;
    }
}