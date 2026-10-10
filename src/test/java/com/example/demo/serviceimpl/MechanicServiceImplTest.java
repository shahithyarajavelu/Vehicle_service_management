package com.example.demo.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.Mechanic;
import com.example.demo.mapper.VehicleServiceMapper;
import com.example.demo.repository.MechanicRepository;
import com.example.demo.responsedto.MechanicResponseDto;

@ExtendWith(MockitoExtension.class)
class MechanicServiceImplTest {

    @Mock
    private MechanicRepository mechanicRepository;

    @Mock
    private VehicleServiceMapper mapper;

    @InjectMocks
    private MechanicServiceImpl mechanicService;

    @Test
    void testGetMechanicById_Success() {

        Mechanic mechanic = new Mechanic();
        mechanic.setMechanicId(1L);
        mechanic.setName("Arun");
        mechanic.setEmail("arun@gmail.com");
        mechanic.setPhone("9876543210");
        mechanic.setSpecialization("Engine");
        mechanic.setExperienceYears(5);

        MechanicResponseDto responseDto = new MechanicResponseDto();
        responseDto.setMechanicId(1L);
        responseDto.setName("Arun");
        responseDto.setEmail("arun@gmail.com");
        responseDto.setPhone("9876543210");
        responseDto.setSpecialization("Engine");
        responseDto.setExperienceYears(5);

        when(mechanicRepository.findById(1L))
                .thenReturn(Optional.of(mechanic));

        when(mapper.toResponse(mechanic))
                .thenReturn(responseDto);

        MechanicResponseDto result =
                mechanicService.getMechanicById(1L);

        assertEquals("Arun", result.getName());
        assertEquals("Engine", result.getSpecialization());
        assertEquals(5, result.getExperienceYears());
    }
}