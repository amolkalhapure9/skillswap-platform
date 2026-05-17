package com.skillswap.userservice;

import com.skillswap.userdto.SkillsDto;
import com.skillswap.userdto.UserSkilsDTO;

public interface UserSkillsService {

    public String saveSkills(UserSkilsDTO dto);

    public String addSkills(SkillsDto dto);

}
