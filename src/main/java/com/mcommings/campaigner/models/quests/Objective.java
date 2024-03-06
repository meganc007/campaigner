package com.mcommings.campaigner.models.quests;

import com.mcommings.campaigner.models.Campaign;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@Entity
@Table(name = "objectives")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Objective extends CommonQuestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    @ManyToOne
    @JoinColumn(name = "fk_campaign_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private Campaign campaign;

    public Objective() {
        super();
    }

    public Objective(int id, String description, UUID fk_campaign_uuid) {
        this.id = id;
        this.setDescription(description);
        this.fk_campaign_uuid = fk_campaign_uuid;
    }

    public Objective(int id, String description, String notes, UUID fk_campaign_uuid) {
        this.id = id;
        this.setDescription(description);
        this.setNotes(notes);
        this.fk_campaign_uuid = fk_campaign_uuid;
    }
}
