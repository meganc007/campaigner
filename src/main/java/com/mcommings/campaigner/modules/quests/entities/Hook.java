package com.mcommings.campaigner.modules.quests.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "hooks")
@NoArgsConstructor
@AllArgsConstructor
public class Hook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String description;
    private String notes;
    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;
}
