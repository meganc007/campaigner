package com.mcommings.campaigner.modules.items.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {

    private int id;
    @NotBlank(message = "Inventory name cannot be empty")
    private String name;
    private String description;
    private UUID fk_campaign_uuid;
    private Integer fk_person;
    private Integer fk_item;
    private Integer fk_weapon;
    private Integer fk_place;
}
