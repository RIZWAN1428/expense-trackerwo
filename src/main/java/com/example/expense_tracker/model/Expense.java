package com.example.expense_tracker.model;

import jakarta.persistence.*;

@Entity   // DB table banaayega
public class Expense {

        @Id   //primary keyy
        @GeneratedValue(strategy = GenerationType.IDENTITY) // id khud banaayega aur auto-increment bhi hogaa
        private int id;

        private String title;
        private double amount;
        private String category;
        private String date;

        // Getters aur setters ka use

        public int getId(){
            return id;
        }
        public int setId(int id){
            return this.id = id;
        }
         public String getTitle() { return title; }
         public void setTitle(String title) { this.title = title; }

         public double getAmount() { return amount; }
         public void setAmount(double amount) { this.amount = amount; }

         public String getCategory() { return category; }
         public void setCategory(String category) { this.category = category; }

         public String getDate() { return date; }
         public void setDate(String date) { this.date = date; }
}
