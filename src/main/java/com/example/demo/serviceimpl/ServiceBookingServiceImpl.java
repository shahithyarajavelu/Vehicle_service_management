package com.example.demo.serviceimpl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Mechanic;
import com.example.demo.entity.ServiceBooking;
import com.example.demo.entity.Vehicle;
import com.example.demo.enums.ServiceStatus;
import com.example.demo.exception.InvalidStatusTransitionException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.VehicleServiceMapper;
import com.example.demo.repository.MechanicRepository;
import com.example.demo.repository.ServiceBookingRepository;
import com.example.demo.repository.VehicleRepository;
import com.example.demo.requestdto.ServiceBookingRequestDto;
import com.example.demo.responsedto.ServiceBookingResponseDto;
import com.example.demo.service.ServiceBookingService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ServiceBookingServiceImpl
        implements ServiceBookingService {

    private final ServiceBookingRepository serviceBookingRepository;
    private final VehicleRepository vehicleRepository;
    private final MechanicRepository mechanicRepository;
    private final VehicleServiceMapper mapper;

    public ServiceBookingServiceImpl(
            ServiceBookingRepository serviceBookingRepository,
            VehicleRepository vehicleRepository,
            MechanicRepository mechanicRepository,
            VehicleServiceMapper mapper) {

        this.serviceBookingRepository = serviceBookingRepository;
        this.vehicleRepository = vehicleRepository;
        this.mechanicRepository = mechanicRepository;
        this.mapper = mapper;
    }

    // CREATE
    @Override
    public ServiceBookingResponseDto createBooking(
            ServiceBookingRequestDto dto) {

        log.info("Creating service booking for vehicle id: {}",
                dto.getVehicleId());

        Vehicle vehicle = vehicleRepository.findById(
                dto.getVehicleId())
                .orElseThrow(() -> {

                    log.warn("Vehicle not found with id: {}",
                            dto.getVehicleId());

                    return new ResourceNotFoundException(
                            "Vehicle not found with id: "
                                    + dto.getVehicleId());
                });

        ServiceBooking booking = mapper.toEntity(dto);

        booking.setVehicle(vehicle);
        booking.setStatus(ServiceStatus.BOOKED);

        if (dto.getMechanicId() != null) {

            Mechanic mechanic = mechanicRepository.findById(
                    dto.getMechanicId())
                    .orElseThrow(() -> {

                        log.warn(
                                "Mechanic not found with id: {}",
                                dto.getMechanicId());

                        return new ResourceNotFoundException(
                                "Mechanic not found with id: "
                                        + dto.getMechanicId());
                    });

            booking.setMechanic(mechanic);
            booking.setStatus(ServiceStatus.ASSIGNED);
        }

        ServiceBooking savedBooking =
                serviceBookingRepository.save(booking);

        log.info(
                "Service booking created successfully with id: {}",
                savedBooking.getBookingId());

        return mapper.toResponse(savedBooking);
    }

    // GET BY ID - CACHE
    @Override
    @Cacheable(value = "serviceBookings", key = "#id")
    public ServiceBookingResponseDto getBookingById(Long id) {

        log.info(
                "Fetching service booking from database with id: {}",
                id);

        ServiceBooking booking =
                serviceBookingRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Service booking not found with id: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Service booking not found with id: "
                                    + id);
                });

        log.info("Service booking found with id: {}", id);

        return mapper.toResponse(booking);
    }

    // GET ALL
    @Override
    public Page<ServiceBookingResponseDto> getAllBookings(
            Pageable pageable) {

        log.info(
                "Fetching service bookings - page: {}, size: {}",
                pageable.getPageNumber(),
                pageable.getPageSize());

        return serviceBookingRepository.findAll(pageable)
                .map(mapper::toResponse);
    }

    // UPDATE - UPDATE CACHE
    @Override
    @CachePut(value = "serviceBookings", key = "#id")
    public ServiceBookingResponseDto updateBooking(
            Long id,
            ServiceBookingRequestDto dto) {

        log.info("Updating service booking with id: {}", id);

        ServiceBooking booking =
                serviceBookingRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Service booking not found with id: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Service booking not found with id: "
                                    + id);
                });

        Vehicle vehicle = vehicleRepository.findById(
                dto.getVehicleId())
                .orElseThrow(() -> {

                    log.warn("Vehicle not found with id: {}",
                            dto.getVehicleId());

                    return new ResourceNotFoundException(
                            "Vehicle not found with id: "
                                    + dto.getVehicleId());
                });

        booking.setBookingDate(dto.getBookingDate());
        booking.setProblemDescription(
                dto.getProblemDescription());
        booking.setVehicle(vehicle);

        if (dto.getMechanicId() != null) {

            Mechanic mechanic = mechanicRepository.findById(
                    dto.getMechanicId())
                    .orElseThrow(() -> {

                        log.warn(
                                "Mechanic not found with id: {}",
                                dto.getMechanicId());

                        return new ResourceNotFoundException(
                                "Mechanic not found with id: "
                                        + dto.getMechanicId());
                    });

            booking.setMechanic(mechanic);
        }

        if (dto.getStatus() != null) {

            validateStatusTransition(
                    booking.getStatus(),
                    dto.getStatus());

            booking.setStatus(dto.getStatus());
        }

        ServiceBooking updatedBooking =
                serviceBookingRepository.save(booking);

        log.info(
                "Service booking updated successfully with id: {}",
                id);

        return mapper.toResponse(updatedBooking);
    }

    // UPDATE STATUS - UPDATE CACHE
    @Override
    @CachePut(value = "serviceBookings", key = "#id")
    public ServiceBookingResponseDto updateBookingStatus(
            Long id,
            ServiceStatus newStatus) {

        log.info(
                "Updating service booking status. id: {}, status: {}",
                id,
                newStatus);

        ServiceBooking booking =
                serviceBookingRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Service booking not found with id: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Service booking not found with id: "
                                    + id);
                });

        validateStatusTransition(
                booking.getStatus(),
                newStatus);

        if (newStatus == ServiceStatus.ASSIGNED
                && booking.getMechanic() == null) {

            log.warn(
                    "Cannot assign booking without mechanic. id: {}",
                    id);

            throw new InvalidStatusTransitionException(
                    "Mechanic must be assigned before changing "
                            + "status to ASSIGNED");
        }

        booking.setStatus(newStatus);

        ServiceBooking updatedBooking =
                serviceBookingRepository.save(booking);

        log.info(
                "Service booking status updated successfully. "
                        + "id: {}, status: {}",
                id,
                newStatus);

        return mapper.toResponse(updatedBooking);
    }

    // DELETE - REMOVE FROM CACHE
    @Override
    @CacheEvict(value = "serviceBookings", key = "#id")
    public void deleteBooking(Long id) {

        log.info("Deleting service booking with id: {}", id);

        ServiceBooking booking =
                serviceBookingRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Service booking not found with id: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Service booking not found with id: "
                                    + id);
                });

        serviceBookingRepository.delete(booking);

        log.info(
                "Service booking deleted successfully with id: {}",
                id);
    }

    // STATUS WORKFLOW
    private void validateStatusTransition(
            ServiceStatus currentStatus,
            ServiceStatus newStatus) {

        if (currentStatus == newStatus) {
            return;
        }

        boolean validTransition =
                (currentStatus == ServiceStatus.BOOKED
                        && newStatus == ServiceStatus.ASSIGNED)
                || (currentStatus == ServiceStatus.ASSIGNED
                        && newStatus == ServiceStatus.IN_PROGRESS)
                || (currentStatus == ServiceStatus.IN_PROGRESS
                        && newStatus == ServiceStatus.COMPLETED)
                || (currentStatus == ServiceStatus.COMPLETED
                        && newStatus == ServiceStatus.DELIVERED)
                || (currentStatus != ServiceStatus.DELIVERED
                        && currentStatus != ServiceStatus.CANCELLED
                        && newStatus == ServiceStatus.CANCELLED);

        if (!validTransition) {

            log.warn(
                    "Invalid status transition from {} to {}",
                    currentStatus,
                    newStatus);

            throw new InvalidStatusTransitionException(
                    "Invalid status transition from "
                            + currentStatus
                            + " to "
                            + newStatus);
        }
    }
}