package com.mcommings.campaigner.modules.common.dtos.government;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateGovernmentDTO {

    @NotNull
    private Integer id;

    private String name;

    private String description;
}
