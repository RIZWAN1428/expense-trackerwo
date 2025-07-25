package com.example.expense_tracker.model;

import jakarta.persistence.*;

@Entity   // DB table banaayega
public class Expense {

        @Id   //primary keyy
        private String id;

        private String title;
        private double amount;
        private String category;
        private String date;

        // Getters aur setters ka use

        public String getId(){
            return id;
        }
        public void setId(String id){
             this.id = id;
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
