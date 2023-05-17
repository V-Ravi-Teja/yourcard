package com.ness.transactionservice.controller;

import com.ness.transactionservice.dto.TransactionDTO;
import com.ness.transactionservice.exception.TransactionNotFound;
import com.ness.transactionservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/transaction")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @PostMapping(value="/createTransaction")
    @ResponseStatus(HttpStatus.CREATED)
    public String addTransaction(@RequestBody TransactionDTO transactionDTO){
            Integer transactionId=transactionService.addTransaction(transactionDTO);
            return "transaction created wit id:"+transactionId;
    }
    @GetMapping(value = "/GetAllTransaction/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionDTO> getAllTransaction(@PathVariable Integer userId) throws TransactionNotFound {
        List<TransactionDTO> transactionDTO=transactionService.getallTransaction(userId);
        return transactionDTO;
    }

    //get transactions by category
    @GetMapping(value = "/TxByCategory/{userId}/{category}")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionDTO> getAllTransaction(@PathVariable Integer userId , @PathVariable String category) throws TransactionNotFound{
        //fetching all transactions of user
        List<TransactionDTO> transactionDTO = transactionService.getallTransaction(userId);
        //filtering transactions by category
        List<TransactionDTO> transactionByCategoryList = transactionService.getTxByCategory(transactionDTO,category);
        return transactionByCategoryList;
    }

    @Transactional
    @PutMapping(value= "/UpdateTransaction/{TransactionId}")
    @ResponseStatus(HttpStatus.OK)
    public String updateTransaction(@PathVariable Integer TransactionId, @RequestBody TransactionDTO transactionDTO)  throws TransactionNotFound{
            transactionService.updateTransactionDetails(TransactionId, transactionDTO);
            return "transaction with id: " + TransactionId + " updated succesfully";
    }

    @Transactional
    @DeleteMapping(value = "/DeleteTransaction/{TransactionId}")
    @ResponseStatus(HttpStatus.OK)
    public String delTransaction(@PathVariable Integer TransactionId) throws TransactionNotFound {
            transactionService.deleteTransaction(TransactionId);
            return " Transaction with id" + TransactionId + " deleted successfully";
    }
}