package com.mcommings.campaigner.modules.items.dtos;

import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Campaign UUID cannot be null or empty.")
    private UUID fk_campaign_uuid;
    private Integer fk_person;
    private Integer fk_item;
    private Integer fk_weapon;
    private Integer fk_place;
}
