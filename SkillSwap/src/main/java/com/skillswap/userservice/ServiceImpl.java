package com.skillswap.userservice;

import com.skillswap.userdto.RegisterDto;
import com.skillswap.userdto.UserProfileDto;
import com.skillswap.userentity.UserEntiity;
import com.skillswap.userenum.Role;
import com.skillswap.userrepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository repository;
    @Autowired
    BCryptPasswordEncoder encoder;



    public String registerUser(RegisterDto dto) throws Exception {
        UserEntiity entity=new UserEntiity();
        entity.setFirstname(dto.getFirstname());
        entity.setLastname(dto.getLastname());
        entity.setEmail(dto.getEmail());
        entity.setBio(dto.getBio());
        entity.setMobileno(dto.getMobileno());
        entity.setUserid(dto.getUserid());
        String pass_word=dto.getPassword();
        entity.setPassword(encoder.encode(pass_word));
        entity.setRole(Role.USER);

        UserEntiity isEmailExist=repository.findByEmail(entity.getEmail());
        UserEntiity isUserIdExists=repository.findByUserid(entity.getUserid());
        UserEntiity user=null;
        if(isEmailExist!=null) {
           return "Email is already exist in system. Please enter unique email";
        }
        else if(isUserIdExists!=null){
            return "userid is already exists in system. Please enter the unique id";
        }
        else{
            user=repository.save(entity);
            return "SUCCESS";
        }







    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntiity user=repository.findByUserid(username);
        return new User(user.getUserid(), user.getPassword(), List.of(new SimpleGrantedAuthority("Role_"+user.getRole().name())));
    }

    public UserProfileDto getUserDetails(String userid){
        UserEntiity entity=repository.findByUserid(userid);

        UserProfileDto dto=new UserProfileDto();
        dto.setFirstname(entity.getFirstname());
        dto.setLastname(entity.getLastname());
        dto.setEmail(entity.getEmail());
        dto.setMobileno(entity.getMobileno());
        dto.setBio(entity.getBio());
        dto.setUserid(entity.getUserid());
        dto.setAbout(entity.getAbout());

        return dto;

    }

    public UserProfileDto updateProfile(UserProfileDto dto){
        UserEntiity entity=repository.findByUserid(dto.getUserid());
        if(entity==null){
            throw new RuntimeException("User not found");
        }

      if(dto.getFirstname()!=null && !dto.getFirstname().equals(entity.getFirstname())){
          entity.setFirstname(dto.getFirstname());
      }
        if(dto.getLastname()!=null && !dto.getLastname().equals(entity.getLastname())){
            entity.setLastname(dto.getLastname());
        }
        if(dto.getBio()!=null && !dto.getBio().equals(entity.getBio())){
            entity.setBio(dto.getBio());
        }
        if(dto.getMobileno()!=null && !dto.getMobileno().equals(entity.getMobileno())){
            entity.setMobileno(dto.getMobileno());
        }
        if(dto.getEmail()!=null && !dto.getEmail().equals(entity.getEmail())){
            entity.setEmail(dto.getEmail());
        }
        if(dto.getAbout()!=null && !dto.getAbout().equals(entity.getAbout())){
            entity.setAbout(dto.getAbout());
        }
        UserEntiity updatedEntity=repository.save(entity);

        return dto;



    }

    public String updateImage(String myuserid, MultipartFile image) throws IOException {
        UserEntiity entity=repository.findByUserid(myuserid);
        entity.setImage(image.getBytes());

        UserEntiity entiity=repository.save(entity);

        if(entiity.getId()!=null){
            return "Success";
        }
        else{
            return "fail";
        }

    }
    public byte[] getProfileImage(String userid){
        UserEntiity entity=repository.findByUserid(userid);

        return entity.getImage();
    }




}
