package com.mcommings.campaigner.models.people;

import com.mcommings.campaigner.models.Campaign;
import com.mcommings.campaigner.models.Event;
import com.mcommings.campaigner.models.locations.Place;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "events_places_people")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class EventPlacePerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_event")
    private Integer fk_event;

    @Column(name = "fk_place")
    private Integer fk_place;

    @Column(name = "fk_person")
    private Integer fk_person;

    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    @ManyToOne
    @JoinColumn(name = "fk_campaign_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "fk_event", referencedColumnName = "id", updatable = false, insertable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "fk_place", referencedColumnName = "id", updatable = false, insertable = false)
    private Place place;

    @ManyToOne
    @JoinColumn(name = "fk_person", referencedColumnName = "id", updatable = false, insertable = false)
    private Person person;

    public EventPlacePerson() {
    }

    public EventPlacePerson(int id, Integer fk_event, Integer fk_place, Integer fk_person, UUID fk_campaign_uuid) {
        this.id = id;
        this.fk_event = fk_event;
        this.fk_place = fk_place;
        this.fk_person = fk_person;
        this.fk_campaign_uuid = fk_campaign_uuid;
    }
}
