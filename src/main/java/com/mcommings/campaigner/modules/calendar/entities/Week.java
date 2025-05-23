package com.mcommings.campaigner.modules.calendar.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "weeks")
@NoArgsConstructor
@AllArgsConstructor
public class Week {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    @Column(nullable = false)
    private UUID fk_campaign_uuid;
    private Integer week_number;
    private Integer fk_month;

}
