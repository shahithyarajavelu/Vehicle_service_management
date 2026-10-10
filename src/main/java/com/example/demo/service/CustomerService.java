package com.example.demo.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.requestdto.CustomerRequestDto;
import com.example.demo.responsedto.CustomerResponseDto;

public interface CustomerService {

    CustomerResponseDto createCustomer(CustomerRequestDto dto);

    CustomerResponseDto getCustomerById(Long id);

    Page<CustomerResponseDto> getAllCustomers(Pageable pageable);

    CustomerResponseDto updateCustomer(Long id, CustomerRequestDto dto);

    void deleteCustomer(Long id);
}