package com.mcommings.campaigner.modules.common.dtos.climate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateClimateDTO {

    @NotNull
    private Integer id;

    private String name;

    private String description;
}
