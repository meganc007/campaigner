package com.mcommings.campaigner.modules.locations.dtos.regions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRegionDTO {

    @NotBlank(message = "City name cannot be empty")
    private String name;

    private String description;

    @NotNull(message = "Campaign UUID cannot be null or empty")
    private UUID campaignUuid;

    @NotNull(message = "Country cannot be empty")
    private Integer countryId;

    @NotNull(message = "Climate cannot be empty")
    private Integer climateId;
}
