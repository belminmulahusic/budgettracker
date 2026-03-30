package com.belminmulahusic.budgettracker.dto.transaction;

import java.math.BigDecimal;

public record MonthlySummaryResponse(
        String month,
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal balance
) {
}