package com.mcommings.campaigner.modules.locations.entities;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.entities.Government;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "countries",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"name", "fk_campaign_uuid"}
                )
        })
@NoArgsConstructor
@AllArgsConstructor
public class Country {

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
    @JoinColumn(name = "fk_continent")
    private Continent continent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_government")
    private Government government;
}
