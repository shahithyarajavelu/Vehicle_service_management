package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "service_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceItem extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceItemId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @Min(value = 1, message = "Unit price must be greater than 0")
    private Double unitPrice;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private ServiceBooking serviceBooking;

    @ManyToOne
    @JoinColumn(name = "part_id", nullable = false)
    private SparePart sparePart;
}