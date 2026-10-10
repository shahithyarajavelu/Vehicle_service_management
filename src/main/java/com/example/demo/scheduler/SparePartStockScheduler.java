package com.example.demo.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.entity.SparePart;
import com.example.demo.repository.SparePartRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SparePartStockScheduler {

    private final SparePartRepository sparePartRepository;

    public SparePartStockScheduler(
            SparePartRepository sparePartRepository) {

        this.sparePartRepository = sparePartRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void checkLowStock() {

        log.info("Checking spare-part stock...");

        List<SparePart> lowStockParts =
                sparePartRepository.findLowStockParts();

        if (lowStockParts.isEmpty()) {

            log.info("All spare parts have sufficient stock.");

        } else {

            log.warn(
                    "Low stock detected for {} spare part(s).",
                    lowStockParts.size()
            );

            for (SparePart part : lowStockParts) {

                log.warn(
                        "Low stock - Part: {}, Part Number: {}, Stock: {}",
                        part.getPartName(),
                        part.getPartNumber(),
                        part.getStockQuantity()
                );
            }
        }
    }
}