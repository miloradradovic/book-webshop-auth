package com.example.authservice.repository;

import com.example.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmailAndPassword(String email, String password);

    User findByEmail(String email);

    List<User> findByEmailOrPhoneNumber(String email, String phoneNumber);

    User findByPhoneNumber(String phoneNumber);

    User findById(int id);
}
