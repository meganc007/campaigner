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
public class EventPlacePersonOverview {
    private int id;
    private Integer fk_event;
    private Integer fk_place;
    private Integer fk_person;
    private UUID fk_campaign_uuid;
    private String eventName;
    private String placeName;
    private String personName;
}
