package com.mcommings.campaigner.modules.common.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClimateDTO {
    private int id;
    @NotBlank(message = "Climate name cannot be empty")
    private String name;
    @NotBlank(message = "Climate description cannot be empty")
    private String description;
}
