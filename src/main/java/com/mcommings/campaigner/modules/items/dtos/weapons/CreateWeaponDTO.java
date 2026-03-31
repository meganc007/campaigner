package com.mcommings.campaigner.modules.items.dtos.weapons;

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
public class CreateWeaponDTO {

    @NotBlank(message = "Weapon name cannot be empty")
    private String name;

    private String description;

    @NotNull(message = "Campaign UUID cannot be null or empty")
    private UUID campaignUuid;

    private String rarity;

    private Integer goldValue;

    private Integer silverValue;

    private Integer copperValue;

    private Float weight;

    private Integer weaponTypeId;

    private Integer damageTypeId;

    private Integer diceTypeId;

    private Integer numberOfDice;

    private Integer damageModifier;

    private Boolean isMagical;

    private Boolean isCursed;

    private String notes;
}
