package com.example.demo.requestdto;

import java.time.LocalDate;

import com.example.demo.enums.ServiceStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceBookingRequestDto {

    @NotNull(message = "Booking date is required")
    private LocalDate bookingDate;

    @Size(
        max = 500,
        message = "Problem description cannot exceed 500 characters"
    )
    private String problemDescription;

    private ServiceStatus status;

    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    private Long mechanicId;
}