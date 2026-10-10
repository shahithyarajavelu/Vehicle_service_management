package com.example.demo.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.SparePart;
import com.example.demo.mapper.VehicleServiceMapper;
import com.example.demo.repository.SparePartRepository;
import com.example.demo.responsedto.SparePartResponseDto;

@ExtendWith(MockitoExtension.class)
class SparePartServiceImplTest {

    @Mock
    private SparePartRepository sparePartRepository;

    @Mock
    private VehicleServiceMapper mapper;

    @InjectMocks
    private SparePartServiceImpl sparePartService;

    @Test
    void testGetSparePartById_Success() {

        SparePart sparePart = new SparePart();
        sparePart.setPartId(1L);
        sparePart.setPartName("Brake Pad");
        sparePart.setPartNumber("BP001");
        sparePart.setPrice(1500.0);
        sparePart.setStockQuantity(10);

        SparePartResponseDto responseDto =
                new SparePartResponseDto();

        responseDto.setPartId(1L);
        responseDto.setPartName("Brake Pad");
        responseDto.setPartNumber("BP001");
        responseDto.setPrice(1500.0);
        responseDto.setStockQuantity(10);

        when(sparePartRepository.findById(1L))
                .thenReturn(Optional.of(sparePart));

        when(mapper.toResponse(sparePart))
                .thenReturn(responseDto);

        SparePartResponseDto result =
                sparePartService.getSparePartById(1L);

        assertEquals("Brake Pad", result.getPartName());
        assertEquals("BP001", result.getPartNumber());
        assertEquals(1500.0, result.getPrice());
    }
}