package com.example.expense_tracker.repository;

import com.example.expense_tracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository :: Spring ka built-in interface for DB
// <Expense, Integer> :: Expense model + uska id type (int)
// Spring khud save(), findAll(), deleteById() jese method de dega — khud likhne ki zarurat nahi

public interface ExpenseRepository extends JpaRepository<Expense, String> {
}