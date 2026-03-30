package com.belminmulahusic.budgettracker.service;

import com.belminmulahusic.budgettracker.dto.transaction.TransactionRequest;
import com.belminmulahusic.budgettracker.dto.transaction.TransactionResponse;
import com.belminmulahusic.budgettracker.entity.Transaction;
import com.belminmulahusic.budgettracker.entity.User;
import com.belminmulahusic.budgettracker.repository.TransactionRepository;
import com.belminmulahusic.budgettracker.repository.UserRepository;
import com.belminmulahusic.budgettracker.util.TransactionType;
import com.belminmulahusic.budgettracker.dto.transaction.CategorySummaryResponse;
import com.belminmulahusic.budgettracker.dto.transaction.MonthlySummaryResponse;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import com.belminmulahusic.budgettracker.util.Category;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionResponse create(TransactionRequest request) {
        User currentUser = getCurrentUser();

        Transaction transaction = Transaction.builder()
                .amount(request.amount())
                .type(request.type())
                .category(request.category())
                .date(request.date())
                .description(request.description())
                .user(currentUser)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToResponse(savedTransaction);
    }

    public List<TransactionResponse> getAll(TransactionType type,
                                            Category category,
                                            LocalDate startDate,
                                            LocalDate endDate,
                                            String month,
                                            String sortBy,
                                            String direction) {
        User currentUser = getCurrentUser();

        if (month != null && !month.isBlank()) {
            YearMonth yearMonth = YearMonth.parse(month);
            startDate = yearMonth.atDay(1);
            endDate = yearMonth.atEndOfMonth();
        }

        var specification = org.springframework.data.jpa.domain.Specification
                .where(TransactionSpecifications.hasUser(currentUser))
                .and(TransactionSpecifications.hasType(type))
                .and(TransactionSpecifications.hasCategory(category))
                .and(TransactionSpecifications.dateGreaterThanOrEqualTo(startDate))
                .and(TransactionSpecifications.dateLessThanOrEqualTo(endDate));

        String safeSortBy;
        if ("amount".equalsIgnoreCase(sortBy)) {
            safeSortBy = "amount";
        } else {
            safeSortBy = "date";
        }

        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return transactionRepository.findAll(specification, Sort.by(sortDirection, safeSortBy))
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TransactionResponse getById(Long id) {
        User currentUser = getCurrentUser();

        Transaction transaction = transactionRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        return mapToResponse(transaction);
    }

    public void delete(Long id) {
        User currentUser = getCurrentUser();

        Transaction transaction = transactionRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transactionRepository.delete(transaction);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCategory(),
                transaction.getDate(),
                transaction.getDescription()
        );
    }

    public TransactionResponse update(Long id, TransactionRequest request) {
        User currentUser = getCurrentUser();

        Transaction transaction = transactionRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setAmount(request.amount());
        transaction.setType(request.type());
        transaction.setCategory(request.category());
        transaction.setDate(request.date());
        transaction.setDescription(request.description());

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToResponse(updatedTransaction);
    }

    public Map<String, BigDecimal> getOverallSummary() {
        User currentUser = getCurrentUser();

        var specification = org.springframework.data.jpa.domain.Specification
                .where(TransactionSpecifications.hasUser(currentUser));

        List<Transaction> transactions = transactionRepository.findAll(specification);

        BigDecimal totalIncome = transactions.stream()
                .filter(transaction -> transaction.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = transactions.stream()
                .filter(transaction -> transaction.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalIncome.subtract(totalExpenses);

        return Map.of(
                "totalIncome", totalIncome,
                "totalExpenses", totalExpenses,
                "balance", balance
        );
    }

    public MonthlySummaryResponse getMonthlySummary(String month) {
        YearMonth yearMonth = YearMonth.parse(month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<TransactionResponse> transactions = getAll(
                null,
                null,
                startDate,
                endDate,
                null,
                "date",
                "desc"
        );

        BigDecimal totalIncome = transactions.stream()
                .filter(transaction -> transaction.type() == TransactionType.INCOME)
                .map(TransactionResponse::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = transactions.stream()
                .filter(transaction -> transaction.type() == TransactionType.EXPENSE)
                .map(TransactionResponse::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalIncome.subtract(totalExpenses);

        return new MonthlySummaryResponse(
                month,
                totalIncome,
                totalExpenses,
                balance
        );
    }

    public List<CategorySummaryResponse> getSummaryByCategory(TransactionType type) {
        List<TransactionResponse> transactions = getAll(
                type,
                null,
                null,
                null,
                null,
                "amount",
                "desc"
        );

        return transactions.stream()
                .collect(Collectors.groupingBy(
                        TransactionResponse::category,
                        Collectors.mapping(
                                TransactionResponse::amount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ))
                .entrySet()
                .stream()
                .map(entry -> new CategorySummaryResponse(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(CategorySummaryResponse::total).reversed())
                .toList();
    }
}