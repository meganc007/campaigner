package com.mcommings.campaigner.modules.locations.dtos.cities;

import jakarta.validation.constraints.NotBlank;
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
public class CreateCityDTO {

    @NotBlank(message = "City name cannot be empty")
    private String name;

    private String description;

    @NotNull(message = "Campaign UUID cannot be null or empty")
    private UUID campaignUuid;

    private Integer wealthId;

    @NotNull(message = "Country cannot be empty")
    private Integer countryId;

    @NotNull(message = "Settlement Type cannot be empty")
    private Integer settlementTypeId;

    private Integer regionId;

    @NotNull(message = "Government cannot be empty")
    private Integer governmentId;
}
