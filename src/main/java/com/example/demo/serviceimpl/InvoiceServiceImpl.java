package com.example.demo.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Invoice;
import com.example.demo.entity.ServiceBooking;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.VehicleServiceMapper;
import com.example.demo.repository.InvoiceRepository;
import com.example.demo.repository.ServiceBookingRepository;
import com.example.demo.responsedto.InvoiceResponseDto;
import com.example.demo.service.InvoiceService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final ServiceBookingRepository serviceBookingRepository;

    private final VehicleServiceMapper mapper;

    public InvoiceServiceImpl(
            InvoiceRepository invoiceRepository,
            ServiceBookingRepository serviceBookingRepository,
            VehicleServiceMapper mapper) {

        this.invoiceRepository = invoiceRepository;
        this.serviceBookingRepository = serviceBookingRepository;
        this.mapper = mapper;
    }

    // CREATE INVOICE

    @Override
    public InvoiceResponseDto createInvoice(Long bookingId) {

        log.info("Creating invoice for booking id: {}", bookingId);

        ServiceBooking booking = serviceBookingRepository
                .findById(bookingId)
                .orElseThrow(() -> {

                    log.warn(
                            "Booking not found with id: {}",
                            bookingId
                    );

                    return new ResourceNotFoundException(
                            "Booking not found with id: " + bookingId
                    );
                });

        Invoice invoice = new Invoice();

        invoice.setTotalAmount(1000.0);
        invoice.setPaymentStatus(
                com.example.demo.enums.PaymentStatus.PENDING
        );
        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setServiceBooking(booking);

        Invoice savedInvoice =
                invoiceRepository.save(invoice);

        log.info(
                "Invoice created successfully with id: {}",
                savedInvoice.getInvoiceId()
        );

        return mapper.toInvoiceResponse(savedInvoice);
    }

    // GET BY ID - CACHE

    @Override
    @Cacheable(value = "invoices", key = "#id")
    public InvoiceResponseDto getInvoiceById(Long id) {

        log.info(
                "Fetching invoice from database with id: {}",
                id
        );

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Invoice not found with id: {}",
                            id
                    );

                    return new ResourceNotFoundException(
                            "Invoice not found with id: " + id
                    );
                });

        log.info("Invoice found with id: {}", id);

        return mapper.toInvoiceResponse(invoice);
    }

    // GET ALL

    @Override
    public List<InvoiceResponseDto> getAllInvoices() {

        log.info("Fetching all invoices");

        List<Invoice> invoices =
                invoiceRepository.findAll();

        log.info(
                "Total invoices found: {}",
                invoices.size()
        );

        return invoices.stream()
                .map(mapper::toInvoiceResponse)
                .toList();
    }
}