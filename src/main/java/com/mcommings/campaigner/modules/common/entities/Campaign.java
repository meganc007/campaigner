package com.mcommings.campaigner.modules.common.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "campaigns")
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {

    @Id
    @GeneratedValue
    @Column(name = "campaign_uuid", nullable = false, updatable = false, unique = true)
    private UUID uuid;
    @Column(nullable = false)
    private String name;
    private String description;
}
