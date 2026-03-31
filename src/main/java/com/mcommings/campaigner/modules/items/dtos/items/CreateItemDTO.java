package com.mcommings.campaigner.modules.items.dtos.items;

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
public class CreateItemDTO {

    @NotBlank(message = "Item name cannot be empty")
    private String name;

    private String description;

    @NotNull(message = "Campaign UUID cannot be null or empty")
    private UUID campaignUuid;

    private String rarity;

    private Integer goldValue;

    private Integer silverValue;

    private Integer copperValue;

    private Float weight;

    private Integer itemTypeId;

    private Boolean isMagical;

    private Boolean isCursed;

    private String notes;
}
