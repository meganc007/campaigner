package com.mcommings.campaigner.modules.items.dtos.weapons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewWeaponDTO {
    private int id;
    private String name;
    private String description;
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
