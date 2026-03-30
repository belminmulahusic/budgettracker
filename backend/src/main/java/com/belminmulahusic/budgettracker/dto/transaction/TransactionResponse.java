package com.belminmulahusic.budgettracker.dto.transaction;

import com.belminmulahusic.budgettracker.util.Category;
import com.belminmulahusic.budgettracker.util.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
        Long id,
        BigDecimal amount,
        TransactionType type,
        Category category,
        LocalDate date,
        String description
) {
}