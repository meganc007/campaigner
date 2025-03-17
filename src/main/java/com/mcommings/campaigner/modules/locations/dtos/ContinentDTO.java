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
public class ContinentDTO {
    private int id;
    @NotBlank(message = "Continent name cannot be empty")
    private String name;
    private String description;
    @NotNull
    private UUID fk_campaign_uuid;
}
