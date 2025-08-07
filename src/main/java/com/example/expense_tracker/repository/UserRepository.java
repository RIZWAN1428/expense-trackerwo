package com.example.expense_tracker.repository;

import com.example.expense_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
//it will lets you access the database without writing SQL.
public interface UserRepository extends JpaRepository<User, Long> {
    //Inherits basic DB operations like save, find, delete for User where id is Long.
    Optional<User> findByUsername(String username);
    //Custom query method – Spring will auto-implement this to find a user by username.
}
