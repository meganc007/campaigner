package com.mcommings.campaigner.modules.common.dtos.wealth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateWealthDTO {

    @NotBlank(message = "Wealth name cannot be empty")
    private String name;
}
