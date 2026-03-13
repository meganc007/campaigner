package com.mcommings.campaigner.modules.locations.dtos.terrains;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTerrainDTO {

    @NotBlank(message = "Region name cannot be empty")
    private String name;

    private String description;
}
