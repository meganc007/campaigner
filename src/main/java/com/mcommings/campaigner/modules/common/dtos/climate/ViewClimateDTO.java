package com.mcommings.campaigner.modules.common.dtos.climate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewClimateDTO {
    private int id;
    private String name;
    private String description;
}
