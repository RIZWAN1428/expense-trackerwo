package com.example.expense_tracker.controller;

import com.example.expense_tracker.model.Expense;
import com.example.expense_tracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController              // Ye class APIs handle karegi
@RequestMapping("/expenses")   // 	Base URL set karta hai
public class ExpenseController {

    @Autowired  // Mujhe ExpenseRepository ka object de do, mujhe khud new nahi banana.
    private ExpenseRepository expenseRepository;

    //POST - Add expense
    @PostMapping                //	/expenses pe POST request handle karega
    public Expense addExpense(@RequestBody Expense expense){
        return expenseRepository.save(expense);
    }

    // GET - Get All Expenses
    @GetMapping                  // /expenses pe GET request karega
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }
}
