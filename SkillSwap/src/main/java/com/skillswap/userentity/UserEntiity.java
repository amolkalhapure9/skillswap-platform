package com.skillswap.userentity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillswap.userenum.Role;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="user")
public class UserEntiity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column
    private String firstname;
    @Column
    private String lastname;
    @Column
    private String userid;
    @Column
    private String email;
    @Column
    private String bio;
    @Column
    private String mobileno;
    @Column
    private Role role;
    @Column
    private String password;
    @Transient
    private String confirmpassword;

    @Column
    private String about;


    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade=CascadeType.ALL)
    private Set<UserSkills> skills;

    @OneToMany(mappedBy = "sender")
    private Set<Connection> sender;

    @OneToMany(mappedBy="receiver")
    private Set<Connection> receiver;

    @Lob
    private byte[] image;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }

    public Set<UserSkills> getSkills() {
        return skills;
    }

    public void setSkills(Set<UserSkills> skills) {
        this.skills = skills;
    }


    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Set<Connection> getReceiver() {
        return receiver;
    }

    public void setReceiver(Set<Connection> receiver) {
        this.receiver = receiver;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Set<Connection> getSender() {
        return sender;
    }

    public void setSender(Set<Connection> sender) {
        this.sender = sender;
    }
}
