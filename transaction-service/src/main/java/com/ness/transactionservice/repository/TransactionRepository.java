package com.ness.transactionservice.repository;
import com.ness.transactionservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId")
    List<Transaction> findAllByUserId(@Param("userId") int userId);

    Transaction getById(Integer transactionId);
}