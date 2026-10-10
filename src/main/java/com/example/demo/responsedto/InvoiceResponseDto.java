package com.example.demo.responsedto;


import java.time.LocalDateTime;

import com.example.demo.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDto {

    private Long invoiceId;

    private Double totalAmount;

    private PaymentStatus paymentStatus;

    private LocalDateTime invoiceDate;

    private Long bookingId;

    private Long vehicleId;

    private String vehicleNumber;
}