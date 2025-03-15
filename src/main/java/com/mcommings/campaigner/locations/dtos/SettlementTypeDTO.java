package com.mcommings.campaigner.locations.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettlementTypeDTO {
    private int id;
    @NotBlank(message = "SettlementType name cannot be empty")
    private String name;
    private String description;
}
