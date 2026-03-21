package com.mcommings.campaigner.modules.common.dtos.campaigns;

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
public class UpdateCampaignDTO {

    @NotNull
    private UUID uuid;

    @NotNull
    private String name;

    private String description;
}
