package com.example.demo.serviceimpl;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Vehicle;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.VehicleServiceMapper;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.VehicleRepository;
import com.example.demo.requestdto.VehicleRequestDto;
import com.example.demo.responsedto.VehicleResponseDto;
import com.example.demo.service.VehicleService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final VehicleServiceMapper mapper;

    public VehicleServiceImpl(
            VehicleRepository vehicleRepository,
            CustomerRepository customerRepository,
            VehicleServiceMapper mapper) {

        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    // CREATE
    @Override
    public VehicleResponseDto createVehicle(VehicleRequestDto dto) {

        log.info("Creating vehicle with number: {}",
                dto.getVehicleNumber());

        if (vehicleRepository.existsByVehicleNumber(
                dto.getVehicleNumber())) {

            log.warn("Vehicle already exists with number: {}",
                    dto.getVehicleNumber());

            throw new DuplicateResourceException(
                    "Vehicle already exists with number: "
                            + dto.getVehicleNumber());
        }

        Customer customer = customerRepository.findById(
                dto.getCustomerId())
                .orElseThrow(() -> {

                    log.warn("Customer not found with id: {}",
                            dto.getCustomerId());

                    return new ResourceNotFoundException(
                            "Customer not found with id: "
                                    + dto.getCustomerId());
                });

        Vehicle vehicle = mapper.toEntity(dto);

        vehicle.setCustomer(customer);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        log.info("Vehicle created successfully with id: {}",
                savedVehicle.getVehicleId());

        return mapper.toResponse(savedVehicle);
    }

    // GET BY ID - CACHE
    @Override
    @Cacheable(value = "vehicles", key = "#id")
    public VehicleResponseDto getVehicleById(Long id) {

        log.info("Fetching vehicle from database with id: {}",
                id);

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Vehicle not found with id: {}", id);

                    return new ResourceNotFoundException(
                            "Vehicle not found with id: " + id);
                });

        log.info("Vehicle found with id: {}", id);

        return mapper.toResponse(vehicle);
    }

    // GET ALL
    @Override
    public Page<VehicleResponseDto> getAllVehicles(
            Pageable pageable) {

        log.info("Fetching vehicles - page: {}, size: {}",
                pageable.getPageNumber(),
                pageable.getPageSize());

        return vehicleRepository.findAll(pageable)
                .map(mapper::toResponse);
    }

    // UPDATE - UPDATE CACHE
    @Override
    @CachePut(value = "vehicles", key = "#id")
    public VehicleResponseDto updateVehicle(
            Long id,
            VehicleRequestDto dto) {

        log.info("Updating vehicle with id: {}", id);

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Vehicle not found with id: {}", id);

                    return new ResourceNotFoundException(
                            "Vehicle not found with id: " + id);
                });

        if (!vehicle.getVehicleNumber()
                .equals(dto.getVehicleNumber())
                && vehicleRepository.existsByVehicleNumber(
                        dto.getVehicleNumber())) {

            log.warn(
                    "Another vehicle already exists with number: {}",
                    dto.getVehicleNumber());

            throw new DuplicateResourceException(
                    "Vehicle already exists with number: "
                            + dto.getVehicleNumber());
        }

        Customer customer = customerRepository.findById(
                dto.getCustomerId())
                .orElseThrow(() -> {

                    log.warn("Customer not found with id: {}",
                            dto.getCustomerId());

                    return new ResourceNotFoundException(
                            "Customer not found with id: "
                                    + dto.getCustomerId());
                });

        vehicle.setVehicleNumber(dto.getVehicleNumber());
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setVehicleType(dto.getVehicleType());
        vehicle.setCustomer(customer);

        Vehicle updatedVehicle =
                vehicleRepository.save(vehicle);

        log.info("Vehicle updated successfully with id: {}",
                id);

        return mapper.toResponse(updatedVehicle);
    }

    // DELETE - REMOVE FROM CACHE
    @Override
    @CacheEvict(value = "vehicles", key = "#id")
    public void deleteVehicle(Long id) {

        log.info("Deleting vehicle with id: {}", id);

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Vehicle not found with id: {}", id);

                    return new ResourceNotFoundException(
                            "Vehicle not found with id: " + id);
                });

        vehicleRepository.delete(vehicle);

        log.info("Vehicle deleted successfully with id: {}",
                id);
    }
    
    @Override
    public List<VehicleResponseDto> getVehiclesByBrand(String brand) {

        log.info("Fetching vehicles by brand: {}", brand);

        return vehicleRepository.findVehiclesByBrand(brand)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<VehicleResponseDto> getVehiclesByType(String vehicleType) {

        log.info("Fetching vehicles by vehicle type: {}", vehicleType);

        return vehicleRepository.findVehiclesByType(vehicleType)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}