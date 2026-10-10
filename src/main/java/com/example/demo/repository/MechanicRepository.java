package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Mechanic;

public interface MechanicRepository
        extends JpaRepository<Mechanic, Long> {
	boolean existsByEmail(String email);
}