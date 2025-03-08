package com.mcommings.campaigner.entities.admin;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User {

    @Id
    @Column(name = "user_uuid", nullable = false, unique = true, columnDefinition = "varchar(36) default gen_random_uuid()")
    private UUID uuid;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "fk_role")
    private Integer fk_role;

    @ManyToOne
    @JoinColumn(name = "fk_role", referencedColumnName = "id", updatable = false, insertable = false)
    private Role role;

    public User() {
        this.uuid = UUID.randomUUID();
    }

    public User(String username, String email, String firstName, String lastName, Integer fk_role) {
        this();
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fk_role = fk_role;
    }

}
