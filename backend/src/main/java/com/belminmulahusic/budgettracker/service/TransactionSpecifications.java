package com.belminmulahusic.budgettracker.service;

import com.belminmulahusic.budgettracker.entity.Transaction;
import com.belminmulahusic.budgettracker.entity.User;
import com.belminmulahusic.budgettracker.util.Category;
import com.belminmulahusic.budgettracker.util.TransactionType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TransactionSpecifications {

    public static Specification<Transaction> hasUser(User user) {
        return (root, query, cb) -> cb.equal(root.get("user"), user);
    }

    public static Specification<Transaction> hasType(TransactionType type) {
        return (root, query, cb) ->
                type == null ? null : cb.equal(root.get("type"), type);
    }

    public static Specification<Transaction> hasCategory(Category category) {
        return (root, query, cb) ->
                category == null ? null : cb.equal(root.get("category"), category);
    }

    public static Specification<Transaction> dateGreaterThanOrEqualTo(LocalDate startDate) {
        return (root, query, cb) ->
                startDate == null ? null : cb.greaterThanOrEqualTo(root.get("date"), startDate);
    }

    public static Specification<Transaction> dateLessThanOrEqualTo(LocalDate endDate) {
        return (root, query, cb) ->
                endDate == null ? null : cb.lessThanOrEqualTo(root.get("date"), endDate);
    }
}