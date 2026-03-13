package com.mcommings.campaigner.modules.locations.dtos.continents;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateContinentDTO {

    @NotNull
    private Integer id;

    private String name;

    private String description;

    private UUID campaignUuid;
}
