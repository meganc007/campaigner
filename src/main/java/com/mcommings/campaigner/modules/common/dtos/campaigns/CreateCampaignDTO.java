package com.mcommings.campaigner.modules.common.dtos.campaigns;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCampaignDTO {

    @NotBlank(message = "Campaign name cannot be empty")
    private String name;

    private String description;
}
