package com.skillswap.userdto;

import com.skillswap.userentity.UserEntiity;
import com.skillswap.userentity.UserSkills;

public class UserStatusDto {

    private String firstname;
    private String lastname;
    private String bio;
    private String status;
    private String userid;

    public UserStatusDto(String firstname,String lastname,String bio, String userid, String status){
       this.firstname=firstname;
       this.lastname=lastname;
       this.bio=bio;
       this.userid=userid;
        this.status=status;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
