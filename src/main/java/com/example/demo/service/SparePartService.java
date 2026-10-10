package com.example.demo.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.requestdto.SparePartRequestDto;
import com.example.demo.responsedto.SparePartResponseDto;

public interface SparePartService {

    SparePartResponseDto createSparePart(SparePartRequestDto dto);

    SparePartResponseDto getSparePartById(Long id);

    Page<SparePartResponseDto> getAllSpareParts(Pageable pageable);

    SparePartResponseDto updateSparePart(Long id, SparePartRequestDto dto);

    void deleteSparePart(Long id);
}