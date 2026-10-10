package com.example.demo.controller;

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

import com.example.demo.requestdto.SparePartRequestDto;
import com.example.demo.responsedto.SparePartResponseDto;
import com.example.demo.service.SparePartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/spare-parts")
@Tag(name = "Spare Part Management", description = "APIs for managing spare parts")
public class SparePartController {

    private final SparePartService sparePartService;

    public SparePartController(SparePartService sparePartService) {
        this.sparePartService = sparePartService;
    }

    @Operation(summary = "Create spare part")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Spare part created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid spare part data"),
        @ApiResponse(responseCode = "409", description = "Spare part already exists")
    })
    @PostMapping
    public ResponseEntity<SparePartResponseDto> createSparePart(
            @Valid @RequestBody SparePartRequestDto dto) {

        return ResponseEntity.ok(
                sparePartService.createSparePart(dto)
        );
    }

    @Operation(summary = "Get spare part by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Spare part found"),
        @ApiResponse(responseCode = "404", description = "Spare part not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SparePartResponseDto> getSparePartById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                sparePartService.getSparePartById(id)
        );
    }

    @Operation(summary = "Get all spare parts")
    @GetMapping
    public ResponseEntity<Page<SparePartResponseDto>> getAllSpareParts(
            Pageable pageable) {

        return ResponseEntity.ok(
                sparePartService.getAllSpareParts(pageable)
        );
    }

    @Operation(summary = "Update spare part")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Spare part updated successfully"),
        @ApiResponse(responseCode = "404", description = "Spare part not found"),
        @ApiResponse(responseCode = "409", description = "Spare part already exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SparePartResponseDto> updateSparePart(
            @PathVariable Long id,
            @Valid @RequestBody SparePartRequestDto dto) {

        return ResponseEntity.ok(
                sparePartService.updateSparePart(id, dto)
        );
    }

    @Operation(summary = "Delete spare part")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Spare part deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Spare part not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSparePart(
            @PathVariable Long id) {

        sparePartService.deleteSparePart(id);
        return ResponseEntity.noContent().build();
    }
}