package com.mcommings.campaigner.modules.items.dtos.dice_types;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDiceTypeDTO {

    @NotNull
    private int id;
    private String name;
    private String description;
    private Integer maxRoll;
}
