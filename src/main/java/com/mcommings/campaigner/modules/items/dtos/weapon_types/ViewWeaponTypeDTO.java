package com.mcommings.campaigner.modules.items.dtos.weapon_types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewWeaponTypeDTO {
    private int id;
    private String name;
    private String description;
}
