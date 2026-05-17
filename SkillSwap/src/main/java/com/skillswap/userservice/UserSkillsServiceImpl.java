package com.skillswap.userservice;

import com.skillswap.userdto.DashBoardDto;
import com.skillswap.userdto.SkillInput;
import com.skillswap.userdto.SkillsDto;
import com.skillswap.userdto.UserSkilsDTO;
import com.skillswap.userentity.Skills;
import com.skillswap.userentity.UserEntiity;
import com.skillswap.userentity.UserSkills;
import com.skillswap.userrepository.ConnectionRepository;
import com.skillswap.userrepository.SkillRepository;
import com.skillswap.userrepository.UserRepository;
import com.skillswap.userrepository.UserSkillsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserSkillsServiceImpl implements UserSkillsService {

    @Autowired
    UserSkillsRepository userskillRepo;
    @Autowired
    UserRepository userRepo;

    @Autowired
    SkillRepository skillRepo;

    @Autowired
    ConnectionRepository connectionRepo;

    @Override
    public String saveSkills(UserSkilsDTO dto) {
        UserEntiity entity= userRepo.findByUserid(dto.getUserid());

        if(entity==null){
            return "User is not exist";
        }
        for(SkillInput skillInput: dto.getSkills()){
          System.out.println(skillInput.getId());
            Skills skill=skillRepo.findById(skillInput.getId());

            if(skill==null){
                return "Skill+"+skillInput.getId() +"+ is not presnt in skill directory";
            }
            UserSkills userSkills=new UserSkills();
            userSkills.setUser(entity);
            userSkills.setSkill(skill);
            userSkills.setCategory(skillInput.getCategory());

            userskillRepo.save(userSkills);


        }

        return "Skills are added";


    }

    @Override
    public String addSkills(SkillsDto dto) {
       Skills skills=new Skills();
       skills.setName(dto.getName());
       Skills skill=skillRepo.save(skills);

       if(skill.getName()!=null){
           return "added";
       }
       else{
           return "Skill is not added";
       }
    }

    public List<Skills> getKnowSkills(String userid, String category){
        UserEntiity entity=userRepo.findByUserid(userid);
        String id=entity.getId();
        List<UserSkills> userSkills= userskillRepo.findAllByUser_Id(id,category);
        List<Skills> skillid=userSkills.stream().map(UserSkills::getSkill).collect(Collectors.toList());
        return skillid;


    }

    public String removeSkill(String userid, int skillid, String category){
        UserEntiity entity=userRepo.findByUserid(userid);
        String id=entity.getId();

        userskillRepo.deleteByUser_Id(id, skillid, category);
        return "Skill deleted";
    }

    public List<Skills> getSkills(){
        List<Skills> skill=skillRepo.findAll();

        return skill;

    }

    public DashBoardDto getDetailsForDashboard(String userid){
   DashBoardDto dto=new DashBoardDto();
        UserEntiity entiity=userRepo.findByUserid(userid);
        String firstname=entiity.getFirstname();
        String lastname=entiity.getLastname();
        dto.setFirstname(firstname);
        dto.setLastname(lastname);
        String user_id=entiity.getId();

        int knowSkills=userskillRepo.getCountofKnowSkills(user_id);

        dto.setKnowskills(""+knowSkills);

        int requestReceived=connectionRepo.getCountOfRequestReceived(user_id);

        dto.setRequestSent(""+requestReceived);



return dto;
    }

}
