package com.mcommings.campaigner.modules.items.dtos.damage_types;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDamageTypeDTO {

    @NotBlank(message = "Damage Type name cannot be empty")
    private String name;

    private String description;
}
