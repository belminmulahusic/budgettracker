package com.belminmulahusic.budgettracker.controller;

import com.belminmulahusic.budgettracker.dto.transaction.TransactionRequest;
import com.belminmulahusic.budgettracker.dto.transaction.TransactionResponse;
import com.belminmulahusic.budgettracker.service.TransactionService;
import com.belminmulahusic.budgettracker.util.TransactionType;
import com.belminmulahusic.budgettracker.util.Category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse create(@Valid @RequestBody TransactionRequest request) {
        return transactionService.create(request);
    }

    @GetMapping
    public List<TransactionResponse> getAll(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        return transactionService.getAll(type, category, startDate, endDate);
    }

    @GetMapping("/{id}")
    public TransactionResponse getById(@PathVariable Long id) {
        return transactionService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        transactionService.delete(id);
    }

    @PutMapping("/{id}")
    public TransactionResponse update(@PathVariable Long id,
                                    @Valid @RequestBody TransactionRequest request) {
        return transactionService.update(id, request);
    }
}