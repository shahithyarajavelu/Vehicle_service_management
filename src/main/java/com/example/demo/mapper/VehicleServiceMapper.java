package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Vehicle;
import com.example.demo.entity.Mechanic;
import com.example.demo.entity.ServiceBooking;
import com.example.demo.entity.SparePart;
import com.example.demo.entity.Invoice;

import com.example.demo.requestdto.CustomerRequestDto;
import com.example.demo.requestdto.VehicleRequestDto;
import com.example.demo.requestdto.MechanicRequestDto;
import com.example.demo.requestdto.ServiceBookingRequestDto;
import com.example.demo.requestdto.SparePartRequestDto;

import com.example.demo.responsedto.CustomerResponseDto;
import com.example.demo.responsedto.VehicleResponseDto;
import com.example.demo.responsedto.MechanicResponseDto;
import com.example.demo.responsedto.ServiceBookingResponseDto;
import com.example.demo.responsedto.SparePartResponseDto;
import com.example.demo.responsedto.InvoiceResponseDto;

@Mapper(componentModel = "spring")
public interface VehicleServiceMapper {

    Customer toEntity(CustomerRequestDto dto);

    CustomerResponseDto toResponse(Customer customer);

    Vehicle toEntity(VehicleRequestDto dto);

    @Mapping(source = "customer.customerId", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    VehicleResponseDto toResponse(Vehicle vehicle);

    Mechanic toEntity(MechanicRequestDto dto);

    MechanicResponseDto toResponse(Mechanic mechanic);

    ServiceBooking toEntity(ServiceBookingRequestDto dto);

    @Mapping(source = "vehicle.vehicleId", target = "vehicleId")
    @Mapping(source = "vehicle.vehicleNumber", target = "vehicleNumber")
    @Mapping(source = "mechanic.mechanicId", target = "mechanicId")
    @Mapping(source = "mechanic.name", target = "mechanicName")
    ServiceBookingResponseDto toResponse(ServiceBooking booking);

    SparePart toEntity(SparePartRequestDto dto);

    SparePartResponseDto toResponse(SparePart sparePart);

    @Mapping(source = "serviceBooking.bookingId", target = "bookingId")
    @Mapping(source = "serviceBooking.vehicle.vehicleId", target = "vehicleId")
    @Mapping(source = "serviceBooking.vehicle.vehicleNumber", target = "vehicleNumber")
    InvoiceResponseDto toInvoiceResponse(Invoice invoice);
}