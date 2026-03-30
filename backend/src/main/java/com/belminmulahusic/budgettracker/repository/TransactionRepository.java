package com.belminmulahusic.budgettracker.repository;

import com.belminmulahusic.budgettracker.entity.Transaction;
import com.belminmulahusic.budgettracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    List<Transaction> findAllByUserOrderByDateDesc(User user);
    Optional<Transaction> findByIdAndUser(Long id, User user);
}