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

import com.example.demo.requestdto.CustomerRequestDto;
import com.example.demo.responsedto.CustomerResponseDto;
import com.example.demo.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
@Tag(
    name = "Customer Management",
    description = "APIs for managing customers"
)
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(
        summary = "Create a customer",
        description = "Registers a new customer in the system"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Customer created successfully"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid customer data"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Customer already exists"
        )
    })
    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(
            @Valid @RequestBody CustomerRequestDto dto) {

        return ResponseEntity.ok(
                customerService.createCustomer(dto)
        );
    }

    @Operation(
        summary = "Get customer by ID",
        description = "Retrieves customer details using customer ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Customer found"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Customer not found"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                customerService.getCustomerById(id)
        );
    }

    @Operation(
        summary = "Get all customers",
        description = "Returns customers with pagination and sorting"
    )
    @GetMapping
    public ResponseEntity<Page<CustomerResponseDto>> getAllCustomers(
            Pageable pageable) {

        return ResponseEntity.ok(
                customerService.getAllCustomers(pageable)
        );
    }

    @Operation(
        summary = "Update customer",
        description = "Updates an existing customer's details"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Customer updated successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Customer not found"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Customer already exists"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequestDto dto) {

        return ResponseEntity.ok(
                customerService.updateCustomer(id, dto)
        );
    }

    @Operation(
        summary = "Delete customer",
        description = "Deletes a customer using customer ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Customer deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Customer not found"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable Long id) {

        customerService.deleteCustomer(id);

        return ResponseEntity.noContent().build();
    }
}