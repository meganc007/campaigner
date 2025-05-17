package com.mcommings.campaigner.modules.people.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @NotNull(message = "Strength cannot be empty.")
    @Positive(message = "Strength must be greater than zero.")
    private int strength;
    @NotNull(message = "Dexterity cannot be empty.")
    @Positive(message = "Dexterity must be greater than zero.")
    private int dexterity;
    @NotNull(message = "Constitution cannot be empty.")
    @Positive(message = "Constitution must be greater than zero.")
    private int constitution;
    @NotNull(message = "Intelligence cannot be empty.")
    @Positive(message = "Intelligence must be greater than zero.")
    private int intelligence;
    @NotNull(message = "Wisdom cannot be empty.")
    @Positive(message = "Wisdom must be greater than zero.")
    private int wisdom;
    @NotNull(message = "Charisma cannot be empty.")
    @Positive(message = "Charisma must be greater than zero.")
    private int charisma;
}
