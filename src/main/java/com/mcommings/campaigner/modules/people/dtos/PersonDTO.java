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
public class PersonDTO {

    private int id;

    @NotBlank(message = "First name cannot be empty.")
    private String firstName;
    private String lastName;
    private int age;
    private String title;
    private Integer fk_race;
    private Integer fk_wealth;
    private Integer fk_ability_score;

    @NotNull(message = "Value must be true or false.")
    private Boolean isNPC;
    @NotNull(message = "Value must be true or false.")
    private Boolean isEnemy;

    private String personality;
    private String description;
    private String notes;

    @NotNull(message = "Campaign UUID cannot be null or empty.")
    private UUID fk_campaign_uuid;
}
