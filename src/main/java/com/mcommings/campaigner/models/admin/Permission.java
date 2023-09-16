package com.mcommings.campaigner.models.admin;

import com.mcommings.campaigner.models.Campaign;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "permissions")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "permission")
    private String permission;

    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    @Column(name = "fk_user_uuid")
    private UUID fk_user_uuid;

    @ManyToOne
    @JoinColumn(name = "fk_campaign_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "fk_user_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private User user;

    public Permission() {
    }

    public Permission(int id, String permission, UUID fk_campaign_uuid, UUID fk_user_uuid) {
        this.id = id;
        this.permission = permission;
        this.fk_campaign_uuid = fk_campaign_uuid;
        this.fk_user_uuid = fk_user_uuid;
    }
}
