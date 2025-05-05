package com.mcommings.campaigner.modules.common.dtos;

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
public class CampaignDTO {
    private UUID uuid;
    @NotBlank(message = "Campaign name cannot be empty")
    private String name;
    private String description;
}
