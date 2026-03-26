package com.mcommings.campaigner.modules.calendar.dtos.moons;

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
public class UpdateMoonDTO {

    @NotNull
    private Integer id;

    private String name;

    private String description;

    private UUID campaignUuid;
}
