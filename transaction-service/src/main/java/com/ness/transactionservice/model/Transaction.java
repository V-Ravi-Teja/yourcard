package com.ness.transactionservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "TransactionId")
    private int transactionId;

    @Column(name = "UserId")
    private int userId;

    @Column(name = "Merchant")
    private String merchant;

    @Column(name = "Amount")
    private BigDecimal amount;

    @Column(name = "Date")
    private LocalDate date;

    @Column(name = "Category")
    private String category;
}