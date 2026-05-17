package com.skillswap.userdto;

import java.util.HashSet;
import java.util.Set;

public class UserSkilsDTO {
    private String userid;
    private Set<SkillInput> skills;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Set<SkillInput> getSkills() {
        return skills;
    }

    public void setSkills(Set<SkillInput> skills) {
        this.skills = skills;
    }
}
