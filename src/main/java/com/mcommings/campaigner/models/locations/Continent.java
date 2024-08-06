package com.mcommings.campaigner.models.locations;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mcommings.campaigner.models.BaseEntity;
import com.mcommings.campaigner.models.Campaign;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "continents")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Continent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "fk_campaign_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private Campaign campaign;

    @JsonManagedReference
    @OneToMany(mappedBy = "continent", fetch = FetchType.EAGER)
    private Set<Country> countries = new HashSet<>();

    public Continent() {
        super();
    }

    public Continent(int id, String name, String description, UUID fk_campaign_uuid) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_campaign_uuid = fk_campaign_uuid;
    }
}
