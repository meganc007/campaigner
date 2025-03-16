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
public class ItemTypeDTO {

    private int id;
    @NotBlank(message = "ItemType name cannot be empty")
    private String name;
    private String description;
}
