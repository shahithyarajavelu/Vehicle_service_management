package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.requestdto.MechanicRequestDto;
import com.example.demo.responsedto.MechanicResponseDto;
import com.example.demo.service.MechanicService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/mechanics")
@Tag(name = "Mechanic Management", description = "APIs for managing mechanics")
public class MechanicController {

    private final MechanicService mechanicService;

    public MechanicController(MechanicService mechanicService) {
        this.mechanicService = mechanicService;
    }

    @Operation(summary = "Create mechanic")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mechanic created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid mechanic data"),
        @ApiResponse(responseCode = "409", description = "Mechanic already exists")
    })
    @PostMapping
    public ResponseEntity<MechanicResponseDto> createMechanic(
            @Valid @RequestBody MechanicRequestDto dto) {

        return ResponseEntity.ok(mechanicService.createMechanic(dto));
    }

    @Operation(summary = "Get mechanic by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mechanic found"),
        @ApiResponse(responseCode = "404", description = "Mechanic not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MechanicResponseDto> getMechanicById(
            @PathVariable Long id) {

        return ResponseEntity.ok(mechanicService.getMechanicById(id));
    }

    @Operation(summary = "Get all mechanics")
    @GetMapping
    public ResponseEntity<Page<MechanicResponseDto>> getAllMechanics(
            Pageable pageable) {

        return ResponseEntity.ok(mechanicService.getAllMechanics(pageable));
    }

    @Operation(summary = "Update mechanic")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mechanic updated successfully"),
        @ApiResponse(responseCode = "404", description = "Mechanic not found"),
        @ApiResponse(responseCode = "409", description = "Mechanic already exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MechanicResponseDto> updateMechanic(
            @PathVariable Long id,
            @Valid @RequestBody MechanicRequestDto dto) {

        return ResponseEntity.ok(mechanicService.updateMechanic(id, dto));
    }

    @Operation(summary = "Delete mechanic")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Mechanic deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Mechanic not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMechanic(
            @PathVariable Long id) {

        mechanicService.deleteMechanic(id);
        return ResponseEntity.noContent().build();
    }
}