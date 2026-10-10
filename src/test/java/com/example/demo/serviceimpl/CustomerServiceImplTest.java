package com.example.demo.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.Customer;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.VehicleServiceMapper;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.requestdto.CustomerRequestDto;
import com.example.demo.responsedto.CustomerResponseDto;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private VehicleServiceMapper mapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void testGetCustomerById_Success() {

        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("John");
        customer.setEmail("john@gmail.com");
        customer.setPhone("9876543210");

        CustomerResponseDto responseDto = new CustomerResponseDto();
        responseDto.setCustomerId(1L);
        responseDto.setName("John");
        responseDto.setEmail("john@gmail.com");
        responseDto.setPhone("9876543210");

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(mapper.toResponse(customer))
                .thenReturn(responseDto);

        CustomerResponseDto result =
                customerService.getCustomerById(1L);

        assertEquals("John", result.getName());
        assertEquals("john@gmail.com", result.getEmail());
    }
    
    @Test
    void testGetCustomerById_NotFound() {

        when(customerRepository.findById(2L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> customerService.getCustomerById(2L)
        );
    }
    
    @Test
    void testCreateCustomer_Success() {

        CustomerRequestDto requestDto = new CustomerRequestDto();
        requestDto.setName("David");
        requestDto.setEmail("david@gmail.com");
        requestDto.setPhone("9876543211");

        Customer customer = new Customer();
        customer.setCustomerId(3L);
        customer.setName("David");
        customer.setEmail("david@gmail.com");
        customer.setPhone("9876543211");

        CustomerResponseDto responseDto = new CustomerResponseDto();
        responseDto.setCustomerId(3L);
        responseDto.setName("David");
        responseDto.setEmail("david@gmail.com");
        responseDto.setPhone("9876543211");

        when(customerRepository.existsByEmail("david@gmail.com"))
                .thenReturn(false);

        when(mapper.toEntity(requestDto))
                .thenReturn(customer);

        when(customerRepository.save(customer))
                .thenReturn(customer);

        when(mapper.toResponse(customer))
                .thenReturn(responseDto);

        CustomerResponseDto result =
                customerService.createCustomer(requestDto);

        assertEquals("David", result.getName());
        assertEquals("david@gmail.com", result.getEmail());
    }
    
    @Test
    void testCreateCustomer_DuplicateEmail() {

        CustomerRequestDto requestDto = new CustomerRequestDto();
        requestDto.setName("David");
        requestDto.setEmail("david@gmail.com");
        requestDto.setPhone("9876543211");

        when(customerRepository.existsByEmail("david@gmail.com"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> customerService.createCustomer(requestDto)
        );
    }
    
    @Test
    void testUpdateCustomer_Success() {

        CustomerRequestDto requestDto = new CustomerRequestDto();
        requestDto.setName("David Updated");
        requestDto.setEmail("davidupdated@gmail.com");
        requestDto.setPhone("9876543212");

        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("David");
        customer.setEmail("david@gmail.com");
        customer.setPhone("9876543211");

        CustomerResponseDto responseDto = new CustomerResponseDto();
        responseDto.setCustomerId(1L);
        responseDto.setName("David Updated");
        responseDto.setEmail("davidupdated@gmail.com");
        responseDto.setPhone("9876543212");

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(customerRepository.existsByEmail("davidupdated@gmail.com"))
                .thenReturn(false);

        when(customerRepository.save(customer))
                .thenReturn(customer);

        when(mapper.toResponse(customer))
                .thenReturn(responseDto);

        CustomerResponseDto result =
                customerService.updateCustomer(1L, requestDto);

        assertEquals("David Updated", result.getName());
        assertEquals("davidupdated@gmail.com", result.getEmail());
    }
}