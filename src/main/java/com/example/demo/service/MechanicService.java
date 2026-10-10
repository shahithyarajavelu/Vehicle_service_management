package com.example.demo.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.requestdto.MechanicRequestDto;
import com.example.demo.responsedto.MechanicResponseDto;

public interface MechanicService {

    MechanicResponseDto createMechanic(MechanicRequestDto dto);

    MechanicResponseDto getMechanicById(Long id);
    Page<MechanicResponseDto> getAllMechanics(Pageable pageable);

    MechanicResponseDto updateMechanic(Long id, MechanicRequestDto dto);

    void deleteMechanic(Long id);
}