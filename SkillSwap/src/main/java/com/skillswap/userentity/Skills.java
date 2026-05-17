package com.skillswap.userentity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="skills")
public class Skills {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    private Set<UserSkills> users;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserSkills> getUsers() {
        return users;
    }

    public void setUsers(Set<UserSkills> users) {
        this.users = users;
    }
}
