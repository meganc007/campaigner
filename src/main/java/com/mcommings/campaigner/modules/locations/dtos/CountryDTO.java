package com.mcommings.campaigner.modules.locations.dtos;

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
public class CountryDTO {

    private int id;
    @NotBlank(message = "Country name cannot be empty")
    private String name;
    private String description;
    @NotNull(message = "Campaign UUID cannot be null or empty.")
    private UUID fk_campaign_uuid;
    private Integer fk_continent;
    private Integer fk_government;
}
