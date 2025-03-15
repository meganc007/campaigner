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
public class LandmarkDTO {

    private int id;
    @NotBlank(message = "Landmark name cannot be empty")
    private String name;
    private String description;
    private UUID fk_campaign_uuid;
    private Integer fk_region;
}
