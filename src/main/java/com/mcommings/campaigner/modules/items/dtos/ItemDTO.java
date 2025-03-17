package com.mcommings.campaigner.modules.items.dtos;

import jakarta.validation.constraints.NotBlank;
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
public class ItemDTO {

    private int id;
    @NotBlank(message = "Item name cannot be empty")
    private String name;
    private String description;
    @NotNull
    private UUID fk_campaign_uuid;
    private String rarity;
    private int gold_value;
    private int silver_value;
    private int copper_value;
    private float weight;
    private Integer fk_item_type;
    private Boolean isMagical;
    private Boolean isCursed;
    private String notes;
}
