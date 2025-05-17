package com.mcommings.campaigner.modules.people.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO {

    private int id;
    @NotBlank(message = "Job name cannot be empty")
    private String name;
    private String description;
}
