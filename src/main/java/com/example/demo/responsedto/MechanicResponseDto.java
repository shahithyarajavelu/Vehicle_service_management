package com.example.demo.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MechanicResponseDto {

    private Long mechanicId;

    private String name;

    private String email;

    private String phone;

    private String specialization;

    private Integer experienceYears;
}