package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    boolean existsByVehicleNumber(String vehicleNumber);

    // JPQL Query
    @Query("""
            SELECT v
            FROM Vehicle v
            WHERE v.brand = :brand
            """)
    List<Vehicle> findVehiclesByBrand(@Param("brand") String brand);

    // Native SQL Query
    @Query(value = """
            SELECT *
            FROM vehicles
            WHERE vehicle_type = :vehicleType
            """, nativeQuery = true)
    List<Vehicle> findVehiclesByType(
            @Param("vehicleType") String vehicleType);
}