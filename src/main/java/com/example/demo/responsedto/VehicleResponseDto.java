package com.example.demo.responsedto;

import com.example.demo.enums.VehicleType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponseDto {

    private Long vehicleId;

    private String vehicleNumber;

    private String brand;

    private String model;

    private VehicleType vehicleType;

    private Long customerId;

    private String customerName;
}