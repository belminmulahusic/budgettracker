package com.belminmulahusic.budgettracker.service;

import com.belminmulahusic.budgettracker.dto.transaction.TransactionRequest;
import com.belminmulahusic.budgettracker.dto.transaction.TransactionResponse;
import com.belminmulahusic.budgettracker.entity.Transaction;
import com.belminmulahusic.budgettracker.entity.User;
import com.belminmulahusic.budgettracker.repository.TransactionRepository;
import com.belminmulahusic.budgettracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    public List<TransactionResponse> getAll() {
        User currentUser = getCurrentUser();

        return transactionRepository.findAllByUserOrderByDateDesc(currentUser)
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
}