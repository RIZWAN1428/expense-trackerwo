package com.example.expense_tracker.controller;

import com.example.expense_tracker.model.Expense;
import com.example.expense_tracker.model.User;
import com.example.expense_tracker.repository.ExpenseRepository;
import com.example.expense_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController              // Ye class APIs handle karegi
@RequestMapping("/expenses")   // 	Base URL set karta hai
@CrossOrigin(origins = "http://localhost:4200")
public class ExpenseController {

    @Autowired  // Mujhe ExpenseRepository ka object de do, mujhe khud new nahi banana.
    private ExpenseRepository expenseRepository;
    @Autowired
    private UserRepository userRepo;

    private String generateShortRandomId(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder id = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            id.append(chars.charAt(random.nextInt(chars.length())));
        }
        return id.toString();
    }


    //POST - Add expense
    @PostMapping                //	/expenses pe POST request handle karega
    public Expense addExpense(@RequestBody Expense expense){
        expense.setId(generateShortRandomId(4));
        return expenseRepository.save(expense);
    }

    // GET - Get All Expenses
    @GetMapping                  // /expenses pe GET request karega
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }
    //GET- get by id
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable String id) {
        Optional<Expense> optionalExpense = expenseRepository.findById(id);
        return optionalExpense.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    // PUT - Update the expenseby Id
    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable String id, @RequestBody Expense expenseDetails, Principal principal) {
        // Cast principal to Authentication and then get User object
        Authentication authentication = (Authentication) principal;
        User principalUser = (User) authentication.getPrincipal();

        User user = userRepo.findByUsername(principalUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isCanEdit() && !user.getRole().equals("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("message", "No permission to edit"));
        }

        Optional<Expense> optionalExpense = expenseRepository.findById(id);
        if (optionalExpense.isPresent()) {
            Expense expense = optionalExpense.get();
            expense.setTitle(expenseDetails.getTitle());
            expense.setAmount(expenseDetails.getAmount());
            expense.setCategory(expenseDetails.getCategory());
            expense.setDate(expenseDetails.getDate());

            Expense updatedExpense = expenseRepository.save(expense);
            return ResponseEntity.ok(updatedExpense);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // DELETE - Delete an expense by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable String id, Principal principal) {
        Authentication authentication = (Authentication) principal;
        User principalUser = (User) authentication.getPrincipal();

        User user = userRepo.findByUsername(principalUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isCanDelete() && !user.getRole().equals("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", "No permission to delete"));
        }

        Optional<Expense> optionalExpense = expenseRepository.findById(id);
        if (optionalExpense.isPresent()) {
            expenseRepository.deleteById(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Expense deleted successfully."));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
