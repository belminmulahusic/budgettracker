package com.belminmulahusic.budgettracker.service;

import com.belminmulahusic.budgettracker.dto.transaction.TransactionRequest;
import com.belminmulahusic.budgettracker.dto.transaction.TransactionResponse;
import com.belminmulahusic.budgettracker.entity.Transaction;
import com.belminmulahusic.budgettracker.entity.User;
import com.belminmulahusic.budgettracker.repository.TransactionRepository;
import com.belminmulahusic.budgettracker.repository.UserRepository;
import com.belminmulahusic.budgettracker.util.TransactionType;

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
}