package com.example.demo.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SparePartResponseDto {

    private Long partId;

    private String partName;

    private String partNumber;

    private Double price;

    private Integer stockQuantity;
}