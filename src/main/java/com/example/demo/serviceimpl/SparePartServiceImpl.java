package com.example.demo.serviceimpl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.SparePart;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.VehicleServiceMapper;
import com.example.demo.repository.SparePartRepository;
import com.example.demo.requestdto.SparePartRequestDto;
import com.example.demo.responsedto.SparePartResponseDto;
import com.example.demo.service.SparePartService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class SparePartServiceImpl implements SparePartService {

    private final SparePartRepository sparePartRepository;
    private final VehicleServiceMapper mapper;

    public SparePartServiceImpl(
            SparePartRepository sparePartRepository,
            VehicleServiceMapper mapper) {

        this.sparePartRepository = sparePartRepository;
        this.mapper = mapper;
    }

    // CREATE
    @Override
    public SparePartResponseDto createSparePart(
            SparePartRequestDto dto) {

        log.info("Creating spare part with part number: {}",
                dto.getPartNumber());

        if (sparePartRepository.existsByPartNumber(
                dto.getPartNumber())) {

            log.warn(
                    "Spare part already exists with part number: {}",
                    dto.getPartNumber());

            throw new DuplicateResourceException(
                    "Spare part already exists with part number: "
                            + dto.getPartNumber());
        }

        SparePart sparePart = mapper.toEntity(dto);

        SparePart savedSparePart =
                sparePartRepository.save(sparePart);

        log.info("Spare part created successfully with id: {}",
                savedSparePart.getPartId());

        return mapper.toResponse(savedSparePart);
    }

    // GET BY ID - CACHE
    @Override
    @Cacheable(value = "spareParts", key = "#id")
    public SparePartResponseDto getSparePartById(Long id) {

        log.info("Fetching spare part from database with id: {}",
                id);

        SparePart sparePart = sparePartRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Spare part not found with id: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Spare part not found with id: " + id);
                });

        log.info("Spare part found with id: {}", id);

        return mapper.toResponse(sparePart);
    }

    // GET ALL
    @Override
    public Page<SparePartResponseDto> getAllSpareParts(
            Pageable pageable) {

        log.info("Fetching spare parts - page: {}, size: {}",
                pageable.getPageNumber(),
                pageable.getPageSize());

        return sparePartRepository.findAll(pageable)
                .map(mapper::toResponse);
    }

    // UPDATE - UPDATE CACHE
    @Override
    @CachePut(value = "spareParts", key = "#id")
    public SparePartResponseDto updateSparePart(
            Long id,
            SparePartRequestDto dto) {

        log.info("Updating spare part with id: {}", id);

        SparePart sparePart = sparePartRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Spare part not found with id: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Spare part not found with id: " + id);
                });

        if (!sparePart.getPartNumber()
                .equals(dto.getPartNumber())
                && sparePartRepository.existsByPartNumber(
                        dto.getPartNumber())) {

            log.warn(
                    "Another spare part already exists with part number: {}",
                    dto.getPartNumber());

            throw new DuplicateResourceException(
                    "Spare part already exists with part number: "
                            + dto.getPartNumber());
        }

        sparePart.setPartName(dto.getPartName());
        sparePart.setPartNumber(dto.getPartNumber());
        sparePart.setPrice(dto.getPrice());
        sparePart.setStockQuantity(dto.getStockQuantity());

        SparePart updatedSparePart =
                sparePartRepository.save(sparePart);

        log.info("Spare part updated successfully with id: {}",
                id);

        return mapper.toResponse(updatedSparePart);
    }

    // DELETE - REMOVE FROM CACHE
    @Override
    @CacheEvict(value = "spareParts", key = "#id")
    public void deleteSparePart(Long id) {

        log.info("Deleting spare part with id: {}", id);

        SparePart sparePart = sparePartRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Spare part not found with id: {}",
                            id);

                    return new ResourceNotFoundException(
                            "Spare part not found with id: " + id);
                });

        sparePartRepository.delete(sparePart);

        log.info("Spare part deleted successfully with id: {}",
                id);
    }
}