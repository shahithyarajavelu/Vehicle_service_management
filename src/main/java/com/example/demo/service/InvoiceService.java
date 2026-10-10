package com.example.demo.service;

import java.util.List;

import com.example.demo.responsedto.InvoiceResponseDto;

public interface InvoiceService {

    InvoiceResponseDto createInvoice(Long bookingId);

    InvoiceResponseDto getInvoiceById(Long id);

    List<InvoiceResponseDto> getAllInvoices();
}