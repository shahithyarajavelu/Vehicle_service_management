package com.example.demo.serviceimpl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Mechanic;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.VehicleServiceMapper;
import com.example.demo.repository.MechanicRepository;
import com.example.demo.requestdto.MechanicRequestDto;
import com.example.demo.responsedto.MechanicResponseDto;
import com.example.demo.service.MechanicService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class MechanicServiceImpl implements MechanicService {

    private final MechanicRepository mechanicRepository;
    private final VehicleServiceMapper mapper;

    public MechanicServiceImpl(
            MechanicRepository mechanicRepository,
            VehicleServiceMapper mapper) {

        this.mechanicRepository = mechanicRepository;
        this.mapper = mapper;
    }

    // CREATE
    @Override
    public MechanicResponseDto createMechanic(MechanicRequestDto dto) {

        log.info("Creating mechanic with email: {}",
                dto.getEmail());

        if (mechanicRepository.existsByEmail(dto.getEmail())) {

            log.warn("Mechanic already exists with email: {}",
                    dto.getEmail());

            throw new DuplicateResourceException(
                    "Mechanic already exists with email: "
                            + dto.getEmail());
        }

        Mechanic mechanic = mapper.toEntity(dto);

        Mechanic savedMechanic =
                mechanicRepository.save(mechanic);

        log.info("Mechanic created successfully with id: {}",
                savedMechanic.getMechanicId());

        return mapper.toResponse(savedMechanic);
    }

    // GET BY ID - CACHE
    @Override
    @Cacheable(value = "mechanics", key = "#id")
    public MechanicResponseDto getMechanicById(Long id) {

        log.info("Fetching mechanic from database with id: {}",
                id);

        Mechanic mechanic = mechanicRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Mechanic not found with id: {}", id);

                    return new ResourceNotFoundException(
                            "Mechanic not found with id: " + id);
                });

        log.info("Mechanic found with id: {}", id);

        return mapper.toResponse(mechanic);
    }

    // GET ALL
    @Override
    public Page<MechanicResponseDto> getAllMechanics(
            Pageable pageable) {

        log.info("Fetching mechanics - page: {}, size: {}",
                pageable.getPageNumber(),
                pageable.getPageSize());

        return mechanicRepository.findAll(pageable)
                .map(mapper::toResponse);
    }

    // UPDATE - UPDATE CACHE
    @Override
    @CachePut(value = "mechanics", key = "#id")
    public MechanicResponseDto updateMechanic(
            Long id,
            MechanicRequestDto dto) {

        log.info("Updating mechanic with id: {}", id);

        Mechanic mechanic = mechanicRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Mechanic not found with id: {}", id);

                    return new ResourceNotFoundException(
                            "Mechanic not found with id: " + id);
                });

        if (!mechanic.getEmail().equals(dto.getEmail())
                && mechanicRepository.existsByEmail(dto.getEmail())) {

            log.warn(
                    "Another mechanic already exists with email: {}",
                    dto.getEmail());

            throw new DuplicateResourceException(
                    "Mechanic already exists with email: "
                            + dto.getEmail());
        }

        mechanic.setName(dto.getName());
        mechanic.setEmail(dto.getEmail());
        mechanic.setPhone(dto.getPhone());
        mechanic.setSpecialization(dto.getSpecialization());
        mechanic.setExperienceYears(dto.getExperienceYears());

        Mechanic updatedMechanic =
                mechanicRepository.save(mechanic);

        log.info("Mechanic updated successfully with id: {}",
                id);

        return mapper.toResponse(updatedMechanic);
    }

    // DELETE - REMOVE FROM CACHE
    @Override
    @CacheEvict(value = "mechanics", key = "#id")
    public void deleteMechanic(Long id) {

        log.info("Deleting mechanic with id: {}", id);

        Mechanic mechanic = mechanicRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Mechanic not found with id: {}", id);

                    return new ResourceNotFoundException(
                            "Mechanic not found with id: " + id);
                });

        mechanicRepository.delete(mechanic);

        log.info("Mechanic deleted successfully with id: {}",
                id);
    }
}