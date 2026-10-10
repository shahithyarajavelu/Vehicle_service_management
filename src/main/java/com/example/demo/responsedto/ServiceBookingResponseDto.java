package com.example.demo.responsedto;

import java.time.LocalDate;

import com.example.demo.enums.ServiceStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceBookingResponseDto {

    private Long bookingId;

    private LocalDate bookingDate;

    private String problemDescription;

    private ServiceStatus status;

    private Long vehicleId;

    private String vehicleNumber;

    private Long mechanicId;

    private String mechanicName;
}