package com.example.demo.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.Invoice;
import com.example.demo.entity.ServiceBooking;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.mapper.VehicleServiceMapper;
import com.example.demo.repository.InvoiceRepository;
import com.example.demo.responsedto.InvoiceResponseDto;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private VehicleServiceMapper mapper;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Test
    void testGetInvoiceById_Success() {

        ServiceBooking booking = new ServiceBooking();
        booking.setBookingId(1L);

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(1L);
        invoice.setTotalAmount(5000.0);
        invoice.setPaymentStatus(PaymentStatus.PENDING);
        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setServiceBooking(booking);

        InvoiceResponseDto responseDto =
                new InvoiceResponseDto();

        responseDto.setInvoiceId(1L);
        responseDto.setTotalAmount(5000.0);
        responseDto.setPaymentStatus(PaymentStatus.PENDING);
        responseDto.setInvoiceDate(invoice.getInvoiceDate());
        responseDto.setBookingId(1L);

        when(invoiceRepository.findById(1L))
                .thenReturn(Optional.of(invoice));

        when(mapper.toInvoiceResponse(invoice))
                .thenReturn(responseDto);

        InvoiceResponseDto result =
                invoiceService.getInvoiceById(1L);

        assertEquals(5000.0, result.getTotalAmount());
        assertEquals(
                PaymentStatus.PENDING,
                result.getPaymentStatus()
        );
        assertEquals(1L, result.getBookingId());
    }
}