package com.mcommings.campaigner.models.calendar;

import com.mcommings.campaigner.models.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "celestial_events")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class CelestialEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_moon")
    private Integer fk_moon;

    @Column(name = "fk_sun")
    private Integer fk_sun;

    @Column(name = "fk_month")
    private Integer fk_month;

    @Column(name = "fk_week")
    private Integer fk_week;

    @Column(name = "fk_day")
    private Integer fk_day;

    @ManyToOne
    @JoinColumn(name = "fk_moon", referencedColumnName = "id", updatable = false, insertable = false)
    private Moon moon;

    @ManyToOne
    @JoinColumn(name = "fk_sun", referencedColumnName = "id", updatable = false, insertable = false)
    private Sun sun;

    @ManyToOne
    @JoinColumn(name = "fk_month", referencedColumnName = "id", updatable = false, insertable = false)
    private Month month;

    @ManyToOne
    @JoinColumn(name = "fk_week", referencedColumnName = "id", updatable = false, insertable = false)
    private Week week;

    @ManyToOne
    @JoinColumn(name = "fk_day", referencedColumnName = "id", updatable = false, insertable = false)
    private Day day;

    @Column(name = "event_year")
    private int event_year;

    public CelestialEvent() {
        super();
    }

    public CelestialEvent(int id, String name, String description, int event_year) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.event_year = event_year;
    }

    public CelestialEvent(int id, String name, String description, Integer fk_moon, Integer fk_sun,
                          Integer fk_month, Integer fk_week, Integer fk_day, int event_year) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_moon = fk_moon;
        this.fk_sun = fk_sun;
        this.fk_month = fk_month;
        this.fk_week = fk_week;
        this.fk_day = fk_day;
        this.event_year = event_year;
    }
}
