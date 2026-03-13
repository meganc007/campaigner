package com.mcommings.campaigner.modules.locations.dtos.places;

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
public class CreatePlaceDTO {

    @NotBlank(message = "Place name cannot be empty")
    private String name;

    private String description;

    @NotNull(message = "Campaign UUID cannot be null or empty")
    private UUID campaignUuid;

    @NotNull(message = "Place Type cannot be empty")
    private Integer placeTypeId;

    @NotNull(message = "Terrain cannot be empty")
    private Integer terrainId;

    @NotNull(message = "Country cannot be empty")
    private Integer countryId;

    private Integer cityId;

    @NotNull(message = "Region cannot be empty")
    private Integer regionId;
}
