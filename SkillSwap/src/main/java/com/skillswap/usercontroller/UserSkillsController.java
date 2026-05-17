package com.skillswap.usercontroller;

import com.skillswap.userdto.SkillsDto;
import com.skillswap.userdto.UserSkilsDTO;
import com.skillswap.userentity.Skills;
import com.skillswap.userrepository.UserSkillsRepository;
import com.skillswap.userservice.UserSkillsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserSkillsController {

    @Autowired
    UserSkillsRepository repository;
    @Autowired
    UserSkillsServiceImpl service;

    @CrossOrigin("*")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/addSkills")
    public ResponseEntity<?> addSkills(@RequestBody UserSkilsDTO dto){

        String saveSkills=service.saveSkills(dto);

        return ResponseEntity.ok(new ApiResponse(true, saveSkills,dto));

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/skillsManagement")
    public ResponseEntity<?> addNewSkills(@RequestBody SkillsDto dto)
    {
            String result=service.addSkills(dto);
            if(result.equals("added")){
                return ResponseEntity.ok(new ApiResponse(true,"Skills are added", dto));
            }
            else{
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Skill is not added", null));
            }
    }

    @CrossOrigin("*")
    @GetMapping("/skills")
    public ResponseEntity<?> userSkills(@RequestParam String userid , @RequestParam String category){
             List<Skills> skills=service.getKnowSkills(userid, category);
             return ResponseEntity.ok(new ApiResponse(true,"Skills are extractedd from database", skills));
    }

    @CrossOrigin("*")
    @DeleteMapping("/removeskills")
    public ResponseEntity<?> removeSkill(@RequestParam String userid,@RequestParam int  skillid, @RequestParam String category){
           String result=service.removeSkill(userid, skillid, category);
           return ResponseEntity.ok(new ApiResponse(true, result, null));
    }

    @CrossOrigin("*")
    @GetMapping("/getSkills")
    public ResponseEntity<?> getAllSkills(){
        List<Skills> skill=service.getSkills();
          return ResponseEntity.ok(new ApiResponse(true, "Skilsl extracted",skill));
    }










}
