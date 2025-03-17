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
public class WeaponDTO {

    private int id;
    @NotBlank(message = "Weapon name cannot be empty")
    private String name;
    private String description;
    @NotNull
    private UUID fk_campaign_uuid;
    private String rarity;
    private int gold_value;
    private int silver_value;
    private int copper_value;
    private float weight;
    private Integer fk_weapon_type;
    private Integer fk_damage_type;
    private Integer fk_dice_type;
    private int number_of_dice;
    private int damage_modifier;
    private Boolean isMagical;
    private Boolean isCursed;
    private String notes;
}
