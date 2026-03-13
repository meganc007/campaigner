package com.mcommings.campaigner.modules.locations.dtos.terrains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewTerrainDTO {
    private int id;
    private String name;
    private String description;
}
