package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.responsedto.InvoiceResponseDto;
import com.example.demo.service.InvoiceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/invoices")
@Tag(
        name = "Invoice Management",
        description = "Manage service invoices"
)
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    @Operation(
            summary = "Create invoice",
            description = "Creates an invoice for a service booking"
    )
    public ResponseEntity<InvoiceResponseDto> createInvoice(
            @RequestParam Long bookingId) {

        InvoiceResponseDto response =
                invoiceService.createInvoice(bookingId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get invoice by ID",
            description = "Fetches an invoice using its ID"
    )
    public ResponseEntity<InvoiceResponseDto> getInvoiceById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                invoiceService.getInvoiceById(id)
        );
    }

    @GetMapping
    @Operation(
            summary = "Get all invoices",
            description = "Fetches all invoices"
    )
    public ResponseEntity<List<InvoiceResponseDto>> getAllInvoices() {

        return ResponseEntity.ok(
                invoiceService.getAllInvoices()
        );
    }
}