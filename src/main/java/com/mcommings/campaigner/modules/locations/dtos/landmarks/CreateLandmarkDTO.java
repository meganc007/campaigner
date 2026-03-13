package com.mcommings.campaigner.modules.locations.dtos.landmarks;

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
public class CreateLandmarkDTO {

    @NotBlank(message = "Landmark name cannot be empty")
    private String name;

    private String description;

    @NotNull(message = "Campaign UUID cannot be null or empty")
    private UUID campaignUuid;

    @NotNull(message = "Region cannot be empty")
    private Integer regionId;
}
