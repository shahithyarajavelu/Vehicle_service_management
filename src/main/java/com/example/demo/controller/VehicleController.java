package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.requestdto.VehicleRequestDto;
import com.example.demo.responsedto.VehicleResponseDto;
import com.example.demo.service.VehicleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/vehicles")
@Tag(name = "Vehicle Management", description = "APIs for managing vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Operation(summary = "Register vehicle")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vehicle registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid vehicle data"),
        @ApiResponse(responseCode = "404", description = "Customer not found"),
        @ApiResponse(responseCode = "409", description = "Vehicle already exists")
    })
    @PostMapping
    public ResponseEntity<VehicleResponseDto> createVehicle(
            @Valid @RequestBody VehicleRequestDto dto) {

        return ResponseEntity.ok(vehicleService.createVehicle(dto));
    }

    @Operation(summary = "Get vehicle by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vehicle found"),
        @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDto> getVehicleById(
            @PathVariable Long id) {

        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @Operation(summary = "Get all vehicles")
    @GetMapping
    public ResponseEntity<Page<VehicleResponseDto>> getAllVehicles(
            Pageable pageable) {

        return ResponseEntity.ok(vehicleService.getAllVehicles(pageable));
    }

    @Operation(summary = "Update vehicle")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vehicle updated successfully"),
        @ApiResponse(responseCode = "404", description = "Vehicle not found"),
        @ApiResponse(responseCode = "409", description = "Vehicle already exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponseDto> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody VehicleRequestDto dto) {

        return ResponseEntity.ok(vehicleService.updateVehicle(id, dto));
    }

    @Operation(summary = "Delete vehicle")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Vehicle deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable Long id) {

        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/brand/{brand}")
    @Operation(
            summary = "Get vehicles by brand",
            description = "Fetches vehicles using a JPQL query"
    )
    public ResponseEntity<List<VehicleResponseDto>> getVehiclesByBrand(
            @PathVariable String brand) {

        return ResponseEntity.ok(
                vehicleService.getVehiclesByBrand(brand)
        );
    }
    
    @GetMapping("/type/{vehicleType}")
    @Operation(
            summary = "Get vehicles by type",
            description = "Fetches vehicles using a native SQL query"
    )
    public ResponseEntity<List<VehicleResponseDto>> getVehiclesByType(
            @PathVariable String vehicleType) {

        return ResponseEntity.ok(
                vehicleService.getVehiclesByType(vehicleType)
        );
    }
}