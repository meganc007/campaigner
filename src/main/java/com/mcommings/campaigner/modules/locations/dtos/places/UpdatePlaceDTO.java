package com.mcommings.campaigner.modules.locations.dtos.places;

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
public class UpdatePlaceDTO {

    @NotNull
    private int id;
    private String name;
    private String description;
    private UUID campaignUuid;
    private Integer placeTypeId;
    private Integer terrainId;
    private Integer countryId;
    private Integer cityId;
    private Integer regionId;
}
