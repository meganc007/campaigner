package com.mcommings.campaigner.modules.locations.dtos.place_types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewPlaceTypeDTO {
    private int id;
    private String name;
    private String description;
}
