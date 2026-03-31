package com.mcommings.campaigner.modules.items.dtos.damage_types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewDamageTypeDTO {
    private int id;
    private String name;
    private String description;
}
