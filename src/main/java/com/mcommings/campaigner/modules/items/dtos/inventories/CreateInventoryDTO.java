package com.mcommings.campaigner.modules.items.dtos.inventories;

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
public class CreateInventoryDTO {

    @NotNull(message = "Campaign UUID cannot be null or empty")
    private UUID campaignUuid;

    private Integer personId;

    private Integer itemId;

    private Integer weaponId;

    private Integer placeId;
}
