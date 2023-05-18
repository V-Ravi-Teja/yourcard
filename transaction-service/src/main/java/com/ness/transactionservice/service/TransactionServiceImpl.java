package com.ness.transactionservice.service;

import com.ness.transactionservice.dto.TransactionDTO;
import com.ness.transactionservice.exception.TransactionNotFound;
import com.ness.transactionservice.model.Transaction;
import com.ness.transactionservice.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    public TransactionRepository transactionRepository;

    @Override
    public Integer addTransaction(TransactionDTO transactionDto){
        Transaction transaction=new Transaction();
        transactionRepository.save(mapToModel(transaction,transactionDto));
        log.info("transaction with id: {} added.",transactionDto.getTransactionId());
        return transaction.getTransactionId();
    }

    @Override
    public List<TransactionDTO> getAllTransaction(Integer userId){
        List<Transaction> transactions=transactionRepository.findAllByUserId(userId);
        List<TransactionDTO> transactionDTOs = transactions.stream().map(this::mapToDTOForList).toList();
        log.info("All transactions of userId: {} fetched.",userId);
        return transactionDTOs;
    }

    @Override
    public TransactionDTO getTransaction(Integer transactionId) throws TransactionNotFound{
        Optional<Transaction> optional = transactionRepository.findById(transactionId);
        Transaction transaction =optional.orElseThrow(() -> new TransactionNotFound("Service.TRANSACTION_NOT_FOUND"));
        return mapToDTO(transaction,new TransactionDTO());
    }

    @Override
    public List<TransactionDTO> getTxByCategory(List<TransactionDTO> T, String category) {
        List<TransactionDTO> txByCategory = T.stream().
                filter(s -> category.equals(s.getCategory())).
                collect(Collectors.toList());
        log.info("transaction with category: {} fetched.",category);
        return txByCategory;
    }

    @Override
    public void updateTransactionDetails(Integer TransactionId, TransactionDTO transactionDTO) throws TransactionNotFound {
        Optional<Transaction> optional = transactionRepository.findById(TransactionId);
        Transaction transaction =optional.orElseThrow(() -> new TransactionNotFound("Service.TRANSACTION_NOT_FOUND"));
        transactionRepository.save(mapToModel(transaction,transactionDTO));
        log.info("transaction with id: {} updated.",TransactionId);
    }

    @Override
    public  void deleteTransaction(Integer TransactionId) throws TransactionNotFound{
        Optional<Transaction> optional = transactionRepository.findById(TransactionId);
        Transaction transaction = optional.orElseThrow(() -> new TransactionNotFound("Service.TRANSACTION_NOT_FOUND"));
        transactionRepository.delete(transaction);
        log.info("transaction with id: {} deleted.",TransactionId);
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