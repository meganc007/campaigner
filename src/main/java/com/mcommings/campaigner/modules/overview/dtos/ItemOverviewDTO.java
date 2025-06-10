package com.mcommings.campaigner.modules.overview.dtos;

import com.mcommings.campaigner.modules.items.dtos.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemOverviewDTO {

    private List<DamageTypeDTO> damageTypes;
    private List<DiceTypeDTO> diceTypes;
    private List<InventoryDTO> inventories;
    private List<ItemDTO> items;
    private List<ItemTypeDTO> itemTypes;
    private List<WeaponDTO> weapons;
    private List<WeaponTypeDTO> weaponTypes;
}
