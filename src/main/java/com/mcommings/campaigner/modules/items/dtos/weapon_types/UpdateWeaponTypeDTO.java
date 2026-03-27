package com.mcommings.campaigner.modules.items.dtos.weapon_types;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWeaponTypeDTO {

    @NotNull
    private int id;
    private String name;
    private String description;
}
