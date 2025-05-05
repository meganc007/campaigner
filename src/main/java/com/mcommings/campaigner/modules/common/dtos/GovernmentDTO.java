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
public class GovernmentDTO {
    private int id;
    @NotBlank(message = "Government name cannot be empty")
    private String name;
    @NotBlank(message = "Government description cannot be empty")
    private String description;
}
