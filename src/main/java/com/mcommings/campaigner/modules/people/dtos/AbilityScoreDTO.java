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
public class AbilityScoreDTO {

    private int id;
    @NotNull(message = "Campaign UUID cannot be null or empty.")
    private UUID fk_campaign_uuid;
    @NotBlank(message = "Strength cannot be empty")
    private int strength;
    @NotBlank(message = "Dexterity cannot be empty")
    private int dexterity;
    @NotBlank(message = "Constitution cannot be empty")
    private int constitution;
    @NotBlank(message = "Intelligence cannot be empty")
    private int intelligence;
    @NotBlank(message = "Wisdom cannot be empty")
    private int wisdom;
    @NotBlank(message = "Charisma cannot be empty")
    private int charisma;
}
