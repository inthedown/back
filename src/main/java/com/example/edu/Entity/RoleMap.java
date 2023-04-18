package com.example.edu.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "role_map",schema = "edu")
public class RoleMap {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "role_name")
    private String roleName;
    @Basic
    @Column(name = "role")
    private String role;





    public RoleMap(long id, String roleName, String role) {
        this.id=id;
        this.roleName=roleName;
        this.role=role;

    }

    public RoleMap() {

    }
}
