package com.mcommings.campaigner.modules.quests.dtos;

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
public class RewardDTO {

    private int id;
    @NotBlank(message = "Reward description cannot be empty")
    private String description;
    private String notes;
    @NotNull(message = "Campaign UUID cannot be null or empty.")
    private UUID fk_campaign_uuid;

    private int gold_value;
    private int silver_value;
    private int copper_value;
    private Integer fk_item;
    private Integer fk_weapon;
}
