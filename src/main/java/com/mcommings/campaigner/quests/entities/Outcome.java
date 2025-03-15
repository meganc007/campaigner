package com.mcommings.campaigner.quests.entities;

import com.mcommings.campaigner.common.entities.Campaign;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@Entity
@Table(name = "outcomes")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Outcome extends CommonQuestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    @ManyToOne
    @JoinColumn(name = "fk_campaign_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private Campaign campaign;

    public Outcome() {
        super();
    }

    public Outcome(int id, String description, UUID fk_campaign_uuid) {
        this.id = id;
        this.setDescription(description);
        this.fk_campaign_uuid = fk_campaign_uuid;
    }

    public Outcome(int id, String description, String notes, UUID fk_campaign_uuid) {
        this.id = id;
        this.setDescription(description);
        this.setNotes(notes);
        this.fk_campaign_uuid = fk_campaign_uuid;
    }
}
