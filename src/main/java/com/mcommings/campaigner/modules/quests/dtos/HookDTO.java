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
public class HookDTO {

    private int id;
    @NotBlank(message = "Hook description cannot be empty")
    private String description;
    private String notes;
    @NotNull(message = "Campaign UUID cannot be null or empty.")
    private UUID fk_campaign_uuid;
}
