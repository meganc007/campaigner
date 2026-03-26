package com.mcommings.campaigner.modules.calendar.entities;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "celestial_events",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"name", "fk_campaign_uuid"}
                )
        })
@NoArgsConstructor
@AllArgsConstructor
public class CelestialEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_campaign_uuid", nullable = false)
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_moon")
    private Moon moon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_sun")
    private Sun sun;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_month", nullable = false)
    private Month month;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_week", nullable = false)
    private Week week;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_day", nullable = false)
    private Day day;

    @Column(name = "event_year", nullable = false)
    private int year;

}
