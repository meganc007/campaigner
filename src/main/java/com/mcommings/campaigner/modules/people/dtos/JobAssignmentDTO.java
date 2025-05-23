package com.mcommings.campaigner.modules.people.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobAssignmentDTO {

    private int id;
    @Positive(message = "Person ID must be greater than zero.")
    private Integer fk_person;
    @Positive(message = "Job ID must be greater than zero.")
    private Integer fk_job;
    @NotNull(message = "Campaign UUID cannot be null or empty.")
    private UUID fk_campaign_uuid;
}
