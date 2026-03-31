package com.mcommings.campaigner.modules.items.dtos.dice_types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewDiceTypeDTO {
    private int id;
    private String name;
    private String description;
    private Integer maxRoll;
}
