package com.example.expense_tracker.controller;

import com.example.expense_tracker.model.Expense;
import com.example.expense_tracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController              // Ye class APIs handle karegi
@RequestMapping("/expenses")   // 	Base URL set karta hai
@CrossOrigin(origins = "http://localhost:4200")
public class ExpenseController {

    @Autowired  // Mujhe ExpenseRepository ka object de do, mujhe khud new nahi banana.
    private ExpenseRepository expenseRepository;

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
    @PutMapping("/{id}")  // PUT API banayi — jo specific id ke expense ko update karegi.
    public ResponseEntity<Expense> updateExpense(@PathVariable String id, @RequestBody Expense expenseDetails) {
       // @PathVariable Long id : URL se {id} value ko le raha hai.
        // @RequestBody Expense expenseDetails : Frontend/Postman se aayi updated data JSON me receive kar raha.
        Optional<Expense> optionalExpense = expenseRepository.findById(id); // DB me dekhta hai ki wo id ka expense present hai ya nahi.
        if (optionalExpense.isPresent()) {  // Agar mila to update karega.
            Expense expense = optionalExpense.get();
            expense.setTitle(expenseDetails.getTitle());  // Har field ko naya value set kar raha.
            expense.setAmount(expenseDetails.getAmount());
            expense.setCategory(expenseDetails.getCategory());
            expense.setDate(expenseDetails.getDate());

            Expense updatedExpense = expenseRepository.save(expense);//Updated expense ko DB me save kar raha.
            return ResponseEntity.ok(updatedExpense); //Agar sab thik hai to 200 OK response dega.
        } else {
            return ResponseEntity.notFound().build();// Agar ID nahi mili to 404 Not Found dega.
        }
    }
    // DELETE - Delete an expense by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteExpense(@PathVariable String id) {
        Optional<Expense> optionalExpense = expenseRepository.findById(id);

        if (optionalExpense.isPresent()) {
            expenseRepository.deleteById(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Expense deleted successfully."));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
