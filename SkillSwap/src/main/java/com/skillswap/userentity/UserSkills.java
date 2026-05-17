package com.skillswap.userentity;

import jakarta.persistence.*;

@Entity
@Table(name="userskills")
public class UserSkills {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntiity user;

    @ManyToOne
    @JoinColumn(name="skill_id")
    private Skills skill;

    @Column
    private String category;


    public UserEntiity getUser() {
        return user;
    }

    public void setUser(UserEntiity user) {
        this.user = user;
    }

    public Skills getSkill() {
        return skill;
    }

    public void setSkill(Skills skill) {
        this.skill = skill;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
