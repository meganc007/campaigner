package com.mcommings.campaigner.modules.locations.dtos.landmarks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewLandmarkDTO {
    private int id;
    private String name;
    private String description;
    private UUID campaignUuid;
    private Integer regionId;
}
