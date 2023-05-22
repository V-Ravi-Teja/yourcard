package com.ness.transactionservice.controller;

import com.ness.transactionservice.dto.*;
import com.ness.transactionservice.exception.TransactionNotFound;
import com.ness.transactionservice.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping(value="/transaction")

public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    RestTemplate restTemplate;

    @PostMapping(value="/createTransaction")
    @ResponseStatus(HttpStatus.CREATED)
    public String addTransaction(@RequestBody TransactionDTO transactionDTO){
    if(transactionService.doesUserExist(transactionDTO.getUserId())){
        Integer transactionId=transactionService.addTransaction(transactionDTO);
        return "transaction created with id:"+transactionId;
    }
    else{
        return  "no user with user id: "+transactionDTO.getUserId();
    }

    }

    @GetMapping(value = "/GetAllTransaction/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionDTO> getAllTransaction(@PathVariable Integer userId){
        if(transactionService.doesUserExist(userId)){
            List<TransactionDTO> transactionDTO = transactionService.getAllTransaction(userId);

            return transactionDTO;
        }
        else{
            return null;
        }
    }
    @GetMapping(value = "/GetAllTxdaywise/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<List<TransactionDTO>> getTransactionsByDayForUser(@PathVariable Integer userId){
        if(transactionService.doesUserExist(userId)){
            List<List<TransactionDTO>> transactionDTO = transactionService.getTransactionsByDayForUser(userId);
            return transactionDTO;
        }
        else{
            return null;
        }
    }

    @GetMapping(value = "/GetAllTxmonthwise/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<List<TransactionDTO>> getTransactionsByMonthForUser(@PathVariable Integer userId){
        if(transactionService.doesUserExist(userId)){
            List<List<TransactionDTO>> transactionDTO = transactionService.getTransactionsByMonthForUser(userId);
            return transactionDTO;
        }
        else{
            return null;
        }
    }
    @GetMapping(value = "/GetAllTxyearwise/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<List<TransactionDTO>> getTransactionsByYearForUser(@PathVariable Integer userId){
        if(transactionService.doesUserExist(userId)){
            List<List<TransactionDTO>> transactionDTO = transactionService.getTransactionsByYearForUser(userId);
            return transactionDTO;
        }
        else{
            return null;
        }
    }
    @GetMapping(value = "/GetTransaction/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public TransactionDTO getTransaction(@PathVariable Integer transactionId) throws TransactionNotFound {
        TransactionDTO transactionDTO = transactionService.getTransaction(transactionId);
        return transactionDTO;
    }

    //get transactions by category
    @GetMapping(value = "/TxByCategory/{userId}/{category}")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionDTO> getTransactionByCategory(@PathVariable Integer userId , @PathVariable String category){
        //fetching all transactions of user
        if(transactionService.doesUserExist(userId)) {

            List<TransactionDTO> transactionDTO = transactionService.getAllTransaction(userId);
            //filtering transactions by category
            List<TransactionDTO> transactionByCategoryList = transactionService.getTxByCategory(transactionDTO, category);
            return transactionByCategoryList;
        }
        else{
            return null;
        }
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
    public String deleteTransaction(@PathVariable Integer TransactionId) throws TransactionNotFound {
            transactionService.deleteTransaction(TransactionId);
            return " Transaction with id" + TransactionId + " deleted successfully";
    }
    @GetMapping("/cardbalanceleft/{userId}")
    public String getCardBalanceLeft(@PathVariable int userId) {
        if(transactionService.doesUserExist(userId)) {
            Integer cardBalanceLeft = transactionService.getCardBalanceLeft(userId);
            return "left balance= " + cardBalanceLeft;
        }
        else{
            return "no user with id: "+userId;
        }
    }

}