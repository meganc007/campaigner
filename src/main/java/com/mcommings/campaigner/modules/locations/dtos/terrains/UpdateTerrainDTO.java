package com.mcommings.campaigner.modules.locations.dtos.terrains;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTerrainDTO {

    @NotNull
    private Integer id;

    private String name;

    private String description;

}
