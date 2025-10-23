package com.mcommings.campaigner.modules.calendar.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(name = "event_year")
    private int eventYear;
    private Integer fk_month;
    private Integer fk_week;
    private Integer fk_day;
    private Integer fk_city;
    private Integer fk_continent;
    private Integer fk_country;
    @Column(nullable = false)
    private UUID fk_campaign_uuid;
}


