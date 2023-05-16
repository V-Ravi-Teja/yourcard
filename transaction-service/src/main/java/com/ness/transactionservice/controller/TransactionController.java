package com.ness.transactionservice.controller;

import com.ness.transactionservice.dto.TransactionDTO;
import com.ness.transactionservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/yourcard")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @PostMapping(value="/createTransaction")
    public String addTransaction(@RequestBody TransactionDTO transactionDTO){
            Integer transactionId=transactionService.addTransaction(transactionDTO);
            return "transaction created wit id:"+transactionId;
    }
    @GetMapping(value = "/GetAllTransaction/{userId}")
    public List<TransactionDTO> getAllTransaction(@PathVariable Integer userId){
        List<TransactionDTO> transactionDTO=transactionService.getallTransaction(userId);
        return transactionDTO;
    }

    //get transactions by category
    @GetMapping(value = "/TxByCategory/{userId}/{category}")
    public List<TransactionDTO> getAllTransaction(@PathVariable Integer userId , @PathVariable String category){
        //fetching all transactions of user
        List<TransactionDTO> transactionDTO = transactionService.getallTransaction(userId);
        //filtering transactions by category
        List<TransactionDTO> transactionByCategoryList = transactionService.getTxByCategory(transactionDTO,category);
        return transactionByCategoryList;
    }

    @Transactional
    @PutMapping(value= "/UpdateTransaction/{TransactionId}")
    public String updateCustomer(@PathVariable Integer TransactionId, @RequestBody TransactionDTO transactionDTO) {
        if(transactionService.checkIfTransactionPresent(TransactionId)) {
            transactionService.updateTransactionDetails(TransactionId, transactionDTO);
            return "transaction with id: " + TransactionId + " updated succesfully";
        }
        else
            return "no such transaction with id: "+TransactionId+" is present";
    }

    @Transactional
    @DeleteMapping(value = "/DeleteTransaction/{TransactionId}")
    public String delTransaction(@PathVariable Integer TransactionId) {
        if (transactionService.checkIfTransactionPresent(TransactionId)) {
            transactionService.deleteTransaction(TransactionId);
            return " Transaction with id" + TransactionId + " deleted successfully";
        } else {
            return "No such transaction present with id: " + TransactionId;
        }
    }
}