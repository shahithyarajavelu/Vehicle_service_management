package com.example.demo.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Vehicle;
import com.example.demo.enums.VehicleType;
import com.example.demo.mapper.VehicleServiceMapper;
import com.example.demo.repository.VehicleRepository;
import com.example.demo.responsedto.VehicleResponseDto;

@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleServiceMapper mapper;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Test
    void testGetVehicleById_Success() {

        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("John");

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(1L);
        vehicle.setVehicleNumber("TN01AB1234");
        vehicle.setBrand("Honda");
        vehicle.setModel("City");
        vehicle.setVehicleType(VehicleType.CAR);
        vehicle.setCustomer(customer);

        VehicleResponseDto responseDto = new VehicleResponseDto();
        responseDto.setVehicleId(1L);
        responseDto.setVehicleNumber("TN01AB1234");
        responseDto.setBrand("Honda");
        responseDto.setModel("City");
        responseDto.setVehicleType(VehicleType.CAR);
        responseDto.setCustomerId(1L);
        responseDto.setCustomerName("John");

        when(vehicleRepository.findById(1L))
                .thenReturn(Optional.of(vehicle));

        when(mapper.toResponse(vehicle))
                .thenReturn(responseDto);

        VehicleResponseDto result =
                vehicleService.getVehicleById(1L);

        assertEquals("TN01AB1234", result.getVehicleNumber());
        assertEquals("Honda", result.getBrand());
        assertEquals("John", result.getCustomerName());
    }
}