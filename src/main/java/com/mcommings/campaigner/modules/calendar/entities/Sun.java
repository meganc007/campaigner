package com.mcommings.campaigner.modules.calendar.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "suns")
@NoArgsConstructor
@AllArgsConstructor
public class Sun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    @Column(nullable = false)
    private UUID fk_campaign_uuid;
}
