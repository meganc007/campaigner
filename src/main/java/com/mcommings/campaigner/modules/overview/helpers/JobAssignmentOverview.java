package com.mcommings.campaigner.modules.overview.helpers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobAssignmentOverview {
    private int id;
    private Integer fk_person;
    private Integer fk_job;
    private UUID fk_campaign_uuid;
    private String personName;
    private String jobName;
}
