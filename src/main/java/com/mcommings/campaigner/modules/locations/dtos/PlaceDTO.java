package com.mcommings.campaigner.modules.locations.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDTO {

    private int id;
    @NotBlank(message = "Place name cannot be empty")
    private String name;
    private String description;
    private UUID fk_campaign_uuid;
    private Integer fk_place_type;
    private Integer fk_terrain;
    private Integer fk_country;
    private Integer fk_city;
    private Integer fk_region;
}
