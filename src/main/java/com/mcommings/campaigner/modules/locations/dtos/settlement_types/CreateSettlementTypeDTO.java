package com.mcommings.campaigner.modules.locations.dtos.settlement_types;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSettlementTypeDTO {

    @NotBlank(message = "Settlement Type name cannot be empty")
    private String name;

    private String description;
}
