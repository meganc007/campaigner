package com.mcommings.campaigner.modules.items.dtos.dice_types;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDiceTypeDTO {

    @NotBlank(message = "Dice Type name cannot be empty")
    private String name;

    private String description;

    @NotNull
    @Min(1)
    private Integer maxRoll;
}
