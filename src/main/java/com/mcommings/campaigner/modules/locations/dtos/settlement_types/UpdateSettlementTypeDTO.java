package com.mcommings.campaigner.modules.locations.dtos.settlement_types;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSettlementTypeDTO {

    @NotNull
    private Integer id;

    private String name;

    private String description;
}
