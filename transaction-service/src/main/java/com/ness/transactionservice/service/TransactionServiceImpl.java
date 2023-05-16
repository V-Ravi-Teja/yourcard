package com.ness.transactionservice.service;

import com.ness.transactionservice.dto.TransactionDTO;
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
        transaction.setTransactionId(transactionDto.getTransactionId());
        transaction.setUserId(transactionDto.getUserId());
        transaction.setMerchant(transactionDto.getMerchant());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDate(transactionDto.getDate());
        transactionRepository.save(transaction);
        return transaction.getTransactionId();
    }

    @Override
    public List<TransactionDTO> getallTransaction(Integer userid){
        List<Transaction> transactions=transactionRepository.findAllByUserId(userid);
        List<TransactionDTO> transactionDTOs = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setTransactionId(transaction.getTransactionId());
            transactionDTO.setUserId(transaction.getUserId());
            transactionDTO.setMerchant(transaction.getMerchant());
            transactionDTO.setAmount(transaction.getAmount());
            transactionDTO.setDate(transaction.getDate());
            transactionDTO.setCategory(transaction.getCategory());
            transactionDTOs.add(transactionDTO);
        }
        return transactionDTOs;
    }
    @Override
    public void updateTransactionDetails(Integer TransactionId, TransactionDTO transactionDTO){

        Optional<Transaction> optional = transactionRepository.findById(TransactionId);
        Transaction transaction =optional.get();
        //transaction.setUser(transactionDTO);
        transaction.setMerchant(transactionDTO.getMerchant());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDate(transactionDTO.getDate());
        //transaction.setCategory(transactionDTO.getCategory());
        transactionRepository.save(transaction);



    }
    @Override
    public  void deleteTransaction(Integer TransactionId){
//        Optional<Transaction> optional = transactionRepository.findById(TransactionId);
//        Transaction transaction = optional.get();
        transactionRepository.deleteByTransactionId(TransactionId);
    }
    public boolean checkIfTransactionPresent(Integer TransactionId) {
        Optional<Transaction> transaction = this.transactionRepository.findById(TransactionId);
        return transaction.isPresent();
    }

    @Override
    public List<TransactionDTO> getTxByCategory(List<TransactionDTO> T, String category) {
        List<TransactionDTO> txByCategory = T.stream().
                filter(s -> category.equals(s.getCategory())).
                collect(Collectors.toList());
        return txByCategory;
    }
}