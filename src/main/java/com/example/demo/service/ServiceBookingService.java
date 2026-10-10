package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.enums.ServiceStatus;
import com.example.demo.requestdto.ServiceBookingRequestDto;
import com.example.demo.responsedto.ServiceBookingResponseDto;

public interface ServiceBookingService {

    ServiceBookingResponseDto createBooking(
            ServiceBookingRequestDto dto);

    ServiceBookingResponseDto getBookingById(Long id);

    Page<ServiceBookingResponseDto> getAllBookings(
            Pageable pageable);

    ServiceBookingResponseDto updateBooking(
            Long id,
            ServiceBookingRequestDto dto);

    ServiceBookingResponseDto updateBookingStatus(
            Long id,
            ServiceStatus status);

    void deleteBooking(Long id);
}