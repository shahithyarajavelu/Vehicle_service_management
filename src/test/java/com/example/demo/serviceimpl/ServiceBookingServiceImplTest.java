package com.example.demo.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.ServiceBooking;
import com.example.demo.entity.Vehicle;
import com.example.demo.entity.Mechanic;
import com.example.demo.enums.ServiceStatus;
import com.example.demo.mapper.VehicleServiceMapper;
import com.example.demo.repository.ServiceBookingRepository;
import com.example.demo.responsedto.ServiceBookingResponseDto;

@ExtendWith(MockitoExtension.class)
class ServiceBookingServiceImplTest {

    @Mock
    private ServiceBookingRepository serviceBookingRepository;

    @Mock
    private VehicleServiceMapper mapper;

    @InjectMocks
    private ServiceBookingServiceImpl serviceBookingService;

    @Test
    void testGetBookingById_Success() {

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(1L);
        vehicle.setVehicleNumber("TN01AB1234");

        Mechanic mechanic = new Mechanic();
        mechanic.setMechanicId(1L);
        mechanic.setName("Arun");

        ServiceBooking booking = new ServiceBooking();
        booking.setBookingId(1L);
        booking.setBookingDate(LocalDate.now());
        booking.setProblemDescription("Engine issue");
        booking.setStatus(ServiceStatus.BOOKED);
        booking.setVehicle(vehicle);
        booking.setMechanic(mechanic);

        ServiceBookingResponseDto responseDto =
                new ServiceBookingResponseDto();

        responseDto.setBookingId(1L);
        responseDto.setBookingDate(booking.getBookingDate());
        responseDto.setProblemDescription("Engine issue");
        responseDto.setStatus(ServiceStatus.BOOKED);
        responseDto.setVehicleId(1L);
        responseDto.setVehicleNumber("TN01AB1234");
        responseDto.setMechanicId(1L);
        responseDto.setMechanicName("Arun");

        when(serviceBookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        when(mapper.toResponse(booking))
                .thenReturn(responseDto);

        ServiceBookingResponseDto result =
                serviceBookingService.getBookingById(1L);

        assertEquals("Engine issue",
                result.getProblemDescription());

        assertEquals(ServiceStatus.BOOKED,
                result.getStatus());

        assertEquals("Arun",
                result.getMechanicName());
    }
}