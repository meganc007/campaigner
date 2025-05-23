package com.mcommings.campaigner.modules.people.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "events_places_people")
@NoArgsConstructor
@AllArgsConstructor
public class EventPlacePerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Integer fk_event;
    private Integer fk_place;
    private Integer fk_person;
    @Column(nullable = false)
    private UUID fk_campaign_uuid;

}
