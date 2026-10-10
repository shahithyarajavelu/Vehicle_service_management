package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.enums.ServiceStatus;
import com.example.demo.requestdto.ServiceBookingRequestDto;
import com.example.demo.responsedto.ServiceBookingResponseDto;
import com.example.demo.service.ServiceBookingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/service-bookings")
@Tag(
    name = "Service Booking Management",
    description = "APIs for managing vehicle service bookings"
)
public class ServiceBookingController {

    private final ServiceBookingService serviceBookingService;

    public ServiceBookingController(
            ServiceBookingService serviceBookingService) {

        this.serviceBookingService = serviceBookingService;
    }

    @Operation(summary = "Create service booking")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Booking created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid booking data"),
        @ApiResponse(responseCode = "404", description = "Vehicle or mechanic not found")
    })
    @PostMapping
    public ResponseEntity<ServiceBookingResponseDto> createBooking(
            @Valid @RequestBody ServiceBookingRequestDto dto) {

        return ResponseEntity.ok(
                serviceBookingService.createBooking(dto)
        );
    }

    @Operation(summary = "Get booking by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Booking found"),
        @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServiceBookingResponseDto> getBookingById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                serviceBookingService.getBookingById(id)
        );
    }

    @Operation(summary = "Get all service bookings")
    @GetMapping
    public ResponseEntity<Page<ServiceBookingResponseDto>> getAllBookings(
            Pageable pageable) {

        return ResponseEntity.ok(
                serviceBookingService.getAllBookings(pageable)
        );
    }

    @Operation(summary = "Update service booking")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Booking updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status transition"),
        @ApiResponse(responseCode = "404", description = "Booking, vehicle or mechanic not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ServiceBookingResponseDto> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody ServiceBookingRequestDto dto) {

        return ResponseEntity.ok(
                serviceBookingService.updateBooking(id, dto)
        );
    }

    @Operation(
        summary = "Update booking status",
        description = "Changes the booking status according to the allowed workflow"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status transition"),
        @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<ServiceBookingResponseDto> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam ServiceStatus status) {

        return ResponseEntity.ok(
                serviceBookingService.updateBookingStatus(id, status)
        );
    }

    @Operation(summary = "Delete service booking")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Booking deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(
            @PathVariable Long id) {

        serviceBookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}