package com.mcommings.campaigner.modules.people.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RaceDTO {

    private int id;
    @NotBlank(message = "Race name cannot be empty")
    private String name;
    private String description;
    @NotNull(message = "Value must be true or false.")
    private Boolean isExotic;
}
