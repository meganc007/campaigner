package com.mcommings.campaigner.modules.locations.dtos.place_types;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlaceTypeDTO {

    @NotBlank(message = "Place Type name cannot be empty")
    private String name;

    private String description;
}
