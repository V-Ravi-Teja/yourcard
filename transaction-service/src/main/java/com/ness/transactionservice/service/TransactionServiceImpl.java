package com.ness.transactionservice.service;

import com.ness.transactionservice.dto.TransactionDTO;
import com.ness.transactionservice.dto.UserDTO;
import com.ness.transactionservice.exception.TransactionNotFound;
import com.ness.transactionservice.model.Transaction;
import com.ness.transactionservice.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    public TransactionRepository transactionRepository;
    @Autowired
    RestTemplate restTemplate;
    private final String userServiceUrl = "http://user-service/user/GetUser/";


    // log messages using log(Level level, String msg)


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
        transaction.setDate(LocalDate.now());
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
    @Override
    public List<List<TransactionDTO>> getTransactionsByDayForUser(Integer userId) {
        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);

        // Group transactions by day
        Map<LocalDate, List<Transaction>> transactionsByDay = transactions.stream()
                .filter(transaction -> transaction.getDate() != null) // Filter out transactions with null date
                .collect(Collectors.groupingBy(Transaction::getDate));

        // Create a list to hold the result
        List<List<TransactionDTO>> transactionsByDayDTO = new ArrayList<>();

        // Convert each group of transactions to DTO and add to the result list
        for (Map.Entry<LocalDate, List<Transaction>> entry : transactionsByDay.entrySet()) {
            List<TransactionDTO> transactionsDTO = entry.getValue().stream()
                    .map(transaction -> mapToDTO(transaction, new TransactionDTO()))
                    .collect(Collectors.toList());
            transactionsByDayDTO.add(transactionsDTO);
        }

        return transactionsByDayDTO;
    }

    @Override
    public List<List<TransactionDTO>> getTransactionsByMonthForUser(Integer userId) {
        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);

        // Group transactions by month
        Map<YearMonth, List<Transaction>> transactionsByMonth = transactions.stream()
                .filter(transaction -> transaction.getDate() != null) // Filter out transactions with null date
                .collect(Collectors.groupingBy(transaction -> YearMonth.from(transaction.getDate())));

        // Create a list to hold the result
        List<List<TransactionDTO>> transactionsByMonthDTO = new ArrayList<>();

        // Convert each group of transactions to DTO and add to the result list
        for (Map.Entry<YearMonth, List<Transaction>> entry : transactionsByMonth.entrySet()) {
            List<TransactionDTO> transactionsDTO = entry.getValue().stream()
                    .map(transaction -> mapToDTO(transaction, new TransactionDTO()))
                    .collect(Collectors.toList());
            transactionsByMonthDTO.add(transactionsDTO);
        }

        return transactionsByMonthDTO;
    }
    @Override
    public List<List<TransactionDTO>> getTransactionsByYearForUser(Integer userId) {
        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);

        // Group transactions by year
        Map<Integer, List<Transaction>> transactionsByYear = transactions.stream()
                .filter(transaction -> transaction.getDate() != null) // Filter out transactions with null date
                .collect(Collectors.groupingBy(transaction -> transaction.getDate().getYear()));

        // Create a list to hold the result
        List<List<TransactionDTO>> transactionsByYearDTO = new ArrayList<>();

        // Convert each group of transactions to DTO and add to the result list
        for (Map.Entry<Integer, List<Transaction>> entry : transactionsByYear.entrySet()) {
            List<TransactionDTO> transactionsDTO = entry.getValue().stream()
                    .map(transaction -> mapToDTO(transaction, new TransactionDTO()))
                    .collect(Collectors.toList());
            transactionsByYearDTO.add(transactionsDTO);
        }

        return transactionsByYearDTO;
    }


    public Integer getCardBalanceLeft(int userId) {

            UserDTO userDTO=restTemplate.getForObject(userServiceUrl + "/" + userId, UserDTO.class);
            if (userDTO!=null) {
            Integer totalTransactionAmount=getTotalTransactionAmountByUser(userId);
            Integer cardBalanceLeft = userDTO.getUserLimit() - totalTransactionAmount;
            return cardBalanceLeft;
            } else {
                return 0;
        }
    }
    public Integer getTotalTransactionAmountByUser(int userId) {
        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);
        Integer totalAmount = 0;
        for (Transaction transaction : transactions) {
            totalAmount += transaction.getAmount().intValue();
        }
        return totalAmount;
    }

    public boolean doesUserExist(int userId) {

          UserDTO userDTO=restTemplate.getForObject(userServiceUrl + "/" + userId, UserDTO.class);
          if(userDTO.getUserId()==userId)
              return true;
          else return false;
    }
}