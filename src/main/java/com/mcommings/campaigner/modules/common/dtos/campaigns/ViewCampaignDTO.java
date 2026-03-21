package com.mcommings.campaigner.modules.common.dtos.campaigns;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewCampaignDTO {
    private UUID uuid;
    private String name;
    private String description;
}
