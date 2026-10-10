package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.SparePart;

public interface SparePartRepository
        extends JpaRepository<SparePart, Long> {
	boolean existsByPartNumber(String partNumber);
	
	@Query("""
            SELECT s
            FROM SparePart s
            WHERE s.stockQuantity <= 5
            """)
    List<SparePart> findLowStockParts();
}