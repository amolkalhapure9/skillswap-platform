package com.skillswap.userservice;

import com.skillswap.userdto.UserConnectionDto;
import com.skillswap.userdto.UserProfileDto;
import com.skillswap.userdto.UserStatusDto;
import com.skillswap.userentity.Connection;
import com.skillswap.userentity.UserEntiity;
import com.skillswap.userrepository.ConnectionRepository;
import com.skillswap.userrepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CollectionServiceImpl {

    @Autowired
    ConnectionRepository connectionRepository;
    @Autowired
    UserRepository userRepository;

    public Page<UserConnectionDto> findUser(String myuser,Pageable pageable){
        Page<UserEntiity> page;
        UserEntiity entity=userRepository.findByUserid(myuser);
        String user_id=entity.getId();

        page= connectionRepository.searchUser(user_id, pageable);



     page.stream().forEach(a->System.out.println(a.getFirstname()));

        return page.map(this::convertToDto);
    }

    private UserConnectionDto convertToDto(UserEntiity entity){

        UserConnectionDto dto = new UserConnectionDto();

        dto.setFirstname(entity.getFirstname());
        dto.setLastname(entity.getLastname());
        dto.setBio(entity.getBio());
        dto.setUserid(entity.getUserid());

        return dto;
    }


    public String addUserConnection(String currentuserid, String myuserid){


        UserEntiity curuser=userRepository.findByUserid(currentuserid);
        UserEntiity myuser=userRepository.findByUserid(myuserid);
        Connection connection=new Connection();
        connection.setIsConnected("N");
        connection.setSender(myuser);
        connection.setReceiver(curuser);




        Connection con=connectionRepository.save(connection);
        if(con.getId()!=null){
            return "Request is sent to the user "+curuser.getFirstname()+" "+curuser.getLastname();
        }
        else{
            return "Request is not sent";
        }
    }

    public Page<UserConnectionDto> findRequestdUser( String myuser, Pageable pageable){

           Page<UserEntiity> page;
           UserEntiity entiity=userRepository.findByUserid(myuser);
           String user_id=entiity.getId();



           page=connectionRepository.searchRequestedUser(user_id,pageable);

           return page.map(this::convertToDto);
    }
    public Page<UserConnectionDto> findReceivedRequest(String keyword, String myuser, Pageable pageable){

        Page<UserEntiity> page;
        UserEntiity entiity=userRepository.findByUserid(myuser);
        String user_id=entiity.getId();

        if(keyword==null) keyword="";
        page=connectionRepository.getReceivedRequests( user_id,pageable);

        return page.map(this::convertToDto);
    }

    public String deleteRequest(String userid, String myuser){

        UserEntiity myuser_id=userRepository.findByUserid(myuser);
        UserEntiity curuser_id=userRepository.findByUserid(userid);

        int rowDeleted=connectionRepository.deleteByReceiverAndSender(myuser_id.getId(), curuser_id.getId());

        if(rowDeleted==1){
            return "Request has been removed";
        }
        else{
            return "Request is not removed please check the details";
        }
    }

    public String approveRequest(String userid, String myuser){
        UserEntiity id1=userRepository.findByUserid(userid);
        UserEntiity id2=userRepository.findByUserid(myuser);
        int rowUpdated=connectionRepository.updateConnectionStatus(id1.getId(),id2.getId());
        if(rowUpdated==1){
            return "Request approved";
        }
        else{
            return "Request rejected";
        }
    }

    public Page<UserConnectionDto> getConnectedUsers(String myuser, Pageable pageable){
        UserEntiity entity=userRepository.findByUserid(myuser);

        String id=entity.getId();
        Page<UserEntiity> users=connectionRepository.getConnectedUser(id, pageable);

        return users.map(this::convertToDto);

    }

    public Page<UserStatusDto> searchUserByKeyword(String keyword, String myuser, Pageable pageable){
          com.skillswap.userentity.UserEntiity entity =userRepository.findByUserid(myuser);
        Page<UserStatusDto> userStatusDto=connectionRepository.searchUserByKeyword(keyword, entity.getId(), pageable);

        return userStatusDto;
    }


}
