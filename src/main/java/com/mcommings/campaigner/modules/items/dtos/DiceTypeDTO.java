package com.mcommings.campaigner.modules.items.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiceTypeDTO {

    private int id;
    @NotBlank(message = "DiceType name cannot be empty")
    private String name;
    private String description;
    private int max_roll;
}
