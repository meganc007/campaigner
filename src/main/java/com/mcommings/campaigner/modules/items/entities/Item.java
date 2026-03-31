package com.mcommings.campaigner.modules.items.entities;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "items")
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_campaign_uuid", nullable = false)
    private Campaign campaign;

    private String rarity;

    @Column(name = "gold_value")
    private int goldValue;

    @Column(name = "silver_value")
    private int silverValue;

    @Column(name = "copper_value")
    private int copperValue;

    private float weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_item_type")
    private ItemType itemType;

    @Column(name = "ismagical")
    private Boolean isMagical;

    @Column(name = "iscursed")
    private Boolean isCursed;

    private String notes;
}
