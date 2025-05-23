package com.mcommings.campaigner.modules.people.dtos;

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
public class GenericMonsterDTO {

    private int id;
    @NotBlank(message = "Monster name cannot be empty")
    private String name;
    private String description;
    @NotNull(message = "Campaign UUID cannot be null or empty.")
    private UUID fk_campaign_uuid;
    private Integer fk_ability_score;
    private String traits;
    private String notes;
}
