package com.mcommings.campaigner.modules.common.entities;

import com.mcommings.campaigner.modules.calendar.entities.Day;
import com.mcommings.campaigner.modules.calendar.entities.Month;
import com.mcommings.campaigner.modules.calendar.entities.Week;
import com.mcommings.campaigner.modules.locations.entities.City;
import com.mcommings.campaigner.modules.locations.entities.Continent;
import com.mcommings.campaigner.modules.locations.entities.Country;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@Entity
@Table(name = "events")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "event_year")
    private int event_year;

    @Column(name = "fk_month")
    private Integer fk_month;

    @Column(name = "fk_week")
    private Integer fk_week;

    @Column(name = "fk_day")
    private Integer fk_day;

    @Column(name = "fk_city")
    private Integer fk_city;

    @Column(name = "fk_continent")
    private Integer fk_continent;

    @Column(name = "fk_country")
    private Integer fk_country;

    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    @ManyToOne
    @JoinColumn(name = "fk_campaign_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "fk_month", referencedColumnName = "id", updatable = false, insertable = false)
    private Month month;

    @ManyToOne
    @JoinColumn(name = "fk_week", referencedColumnName = "id", updatable = false, insertable = false)
    private Week week;

    @ManyToOne
    @JoinColumn(name = "fk_day", referencedColumnName = "id", updatable = false, insertable = false)
    private Day day;

    @ManyToOne
    @JoinColumn(name = "fk_city", referencedColumnName = "id", updatable = false, insertable = false)
    private City city;

    @ManyToOne
    @JoinColumn(name = "fk_continent", referencedColumnName = "id", updatable = false, insertable = false)
    private Continent continent;

    @ManyToOne
    @JoinColumn(name = "fk_country", referencedColumnName = "id", updatable = false, insertable = false)
    private Country country;

    public Event() {
        super();
    }

    public Event(int id, String name, String description, int event_year, UUID fk_campaign_uuid) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.event_year = event_year;
        this.fk_campaign_uuid = fk_campaign_uuid;
    }

    public Event(int id, String name, String description, int event_year, Integer fk_month, Integer fk_week,
                 Integer fk_day, Integer fk_city, Integer fk_continent, Integer fk_country, UUID fk_campaign_uuid) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.event_year = event_year;
        this.fk_month = fk_month;
        this.fk_week = fk_week;
        this.fk_day = fk_day;
        this.fk_city = fk_city;
        this.fk_continent = fk_continent;
        this.fk_country = fk_country;
        this.fk_campaign_uuid = fk_campaign_uuid;
    }
}


