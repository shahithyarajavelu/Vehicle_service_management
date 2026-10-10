package com.example.demo.serviceimpl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Customer;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.VehicleServiceMapper;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.requestdto.CustomerRequestDto;
import com.example.demo.responsedto.CustomerResponseDto;
import com.example.demo.service.CustomerService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final VehicleServiceMapper mapper;

    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            VehicleServiceMapper mapper) {

        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    // CREATE
    @Override
    public CustomerResponseDto createCustomer(CustomerRequestDto dto) {

        log.info("Creating customer with email: {}", dto.getEmail());

        if (customerRepository.existsByEmail(dto.getEmail())) {

            log.warn("Customer already exists with email: {}",
                    dto.getEmail());

            throw new DuplicateResourceException(
                    "Customer already exists with email: "
                            + dto.getEmail());
        }

        Customer customer = mapper.toEntity(dto);

        Customer savedCustomer = customerRepository.save(customer);

        log.info("Customer created successfully with id: {}",
                savedCustomer.getCustomerId());

        return mapper.toResponse(savedCustomer);
    }

    // GET BY ID - CACHE
    @Override
    @Cacheable(value = "customers", key = "#id")
    public CustomerResponseDto getCustomerById(Long id) {

        log.info("Fetching customer from database with id: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Customer not found with id: {}", id);

                    return new ResourceNotFoundException(
                            "Customer not found with id: " + id);
                });

        log.info("Customer found with id: {}", id);

        return mapper.toResponse(customer);
    }

    // GET ALL
    @Override
    public Page<CustomerResponseDto> getAllCustomers(
            Pageable pageable) {

        log.info("Fetching customers - page: {}, size: {}",
                pageable.getPageNumber(),
                pageable.getPageSize());

        return customerRepository.findAll(pageable)
                .map(mapper::toResponse);
    }

    // UPDATE - UPDATE CACHE
    @Override
    @CachePut(value = "customers", key = "#id")
    public CustomerResponseDto updateCustomer(
            Long id,
            CustomerRequestDto dto) {

        log.info("Updating customer with id: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Customer not found with id: {}", id);

                    return new ResourceNotFoundException(
                            "Customer not found with id: " + id);
                });

        if (!customer.getEmail().equals(dto.getEmail())
                && customerRepository.existsByEmail(dto.getEmail())) {

            log.warn("Another customer already exists with email: {}",
                    dto.getEmail());

            throw new DuplicateResourceException(
                    "Customer already exists with email: "
                            + dto.getEmail());
        }

        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());

        Customer updatedCustomer =
                customerRepository.save(customer);

        log.info("Customer updated successfully with id: {}",
                id);

        return mapper.toResponse(updatedCustomer);
    }

    // DELETE - REMOVE FROM CACHE
    @Override
    @CacheEvict(value = "customers", key = "#id")
    public void deleteCustomer(Long id) {

        log.info("Deleting customer with id: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Customer not found with id: {}", id);

                    return new ResourceNotFoundException(
                            "Customer not found with id: " + id);
                });

        customerRepository.delete(customer);

        log.info("Customer deleted successfully with id: {}",
                id);
    }
}