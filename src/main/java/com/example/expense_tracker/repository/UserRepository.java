package com.example.expense_tracker.repository;

import com.example.expense_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
//it will lets you access the database without writing SQL.
public interface UserRepository extends JpaRepository<User, Long> {
    //Inherits basic DB operations like save, find, delete for User where id is Long.
    Optional<User> findByUsername(String username);
    //Spring will auto-make a query:
    //“SELECT * FROM user WHERE username = ?”
    //Optional<User> means: result may exist or be empty.
}
