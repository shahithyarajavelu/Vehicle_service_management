package com.example.demo.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.requestdto.VehicleRequestDto;
import com.example.demo.responsedto.VehicleResponseDto;

public interface VehicleService {

    VehicleResponseDto createVehicle(VehicleRequestDto dto);

    VehicleResponseDto getVehicleById(Long id);

    Page<VehicleResponseDto> getAllVehicles(Pageable pageable);

    VehicleResponseDto updateVehicle(Long id, VehicleRequestDto dto);
    
    List<VehicleResponseDto> getVehiclesByBrand(String brand);

    List<VehicleResponseDto> getVehiclesByType(String vehicleType);

    void deleteVehicle(Long id);
}