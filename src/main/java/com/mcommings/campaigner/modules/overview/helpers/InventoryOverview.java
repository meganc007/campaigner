package com.mcommings.campaigner.modules.overview.helpers;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryOverview {
    private int id;
    private UUID fk_campaign_uuid;
    private Integer fk_person;
    private Integer fk_item;
    private Integer fk_weapon;
    private Integer fk_place;
    private String personName;
    private String placeName;
}
