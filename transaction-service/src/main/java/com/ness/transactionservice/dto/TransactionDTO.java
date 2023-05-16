package com.ness.transactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private int transactionId;
    private int userId;
    private String merchant;
    private BigDecimal amount;
    private LocalDate date;
    private String category;
}
