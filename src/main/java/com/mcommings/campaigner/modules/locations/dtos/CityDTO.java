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
public class CityDTO {

    private int id;
    @NotBlank(message = "City name cannot be empty")
    private String name;
    private String description;
    @NotNull
    private UUID fk_campaign_uuid;
    private Integer fk_wealth;
    private Integer fk_country;
    private Integer fk_settlement;
    private Integer fk_government;
    private Integer fk_region;
}
