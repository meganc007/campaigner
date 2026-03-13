package com.mcommings.campaigner.modules.locations.entities;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.entities.Government;
import com.mcommings.campaigner.modules.common.entities.Wealth;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "cities",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"name", "fk_campaign_uuid"}
                )
        })
@NoArgsConstructor
@AllArgsConstructor
public class City {

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
    @JoinColumn(name = "fk_wealth")
    private Wealth wealth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_country")
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_settlement")
    private SettlementType settlementType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_government")
    private Government government;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_region")
    private Region region;
}
