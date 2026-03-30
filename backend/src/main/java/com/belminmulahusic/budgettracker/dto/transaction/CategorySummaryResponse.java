package com.belminmulahusic.budgettracker.dto.transaction;

import com.belminmulahusic.budgettracker.util.Category;

import java.math.BigDecimal;

public record CategorySummaryResponse(
        Category category,
        BigDecimal total
) {
}