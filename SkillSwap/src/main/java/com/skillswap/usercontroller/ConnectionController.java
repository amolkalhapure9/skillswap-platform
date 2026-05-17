package com.skillswap.usercontroller;

import com.skillswap.userdto.UserConnectionDto;
import com.skillswap.userdto.UserProfileDto;
import com.skillswap.userdto.UserStatusDto;
import com.skillswap.userservice.CollectionServiceImpl;
import jakarta.transaction.Transactional;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ConnectionController {
    @Autowired
    CollectionServiceImpl collectionService;

    @CrossOrigin("*")
    @GetMapping("/userlist")
    public ResponseEntity<?> userList(@PageableDefault(page=0, size=10) Pageable pageable,
    @RequestParam(defaultValue="firstname") String sortBy,
    @RequestParam(defaultValue = "desc") String direction,
                                      @RequestParam("myuser") String myuser)



    {
      Sort sort=direction.equalsIgnoreCase("desc")?
              Sort.by(sortBy).descending():
              Sort.by(sortBy).ascending();

      Pageable sortPageable= PageRequest.of(
              pageable.getPageNumber(),
              pageable.getPageSize(),
              sort
      );
        Page<UserConnectionDto> userDto=collectionService.findUser(myuser,sortPageable);



        return ResponseEntity.ok(new ApiResponse(true, "Users extracted", userDto));



    }

    @CrossOrigin("*")
    @PostMapping("/sendRequest")
    public ResponseEntity<?> sendConnectionRequest(@RequestParam("currentuserid") String currentuserid,
                                                   @RequestParam("myuserid") String myuserid)
    {

        String str=collectionService.addUserConnection(currentuserid, myuserid);





     return ResponseEntity.ok(new ApiResponse(true, "Response is generated",str));

    }


    @CrossOrigin("*")
    @GetMapping("/requested-user")
    public ResponseEntity<?> getRequestedUserList(@PageableDefault(page=0, size=10) Pageable pageable,
                                                  @RequestParam(defaultValue="firstname") String sortBy,
                                                  @RequestParam(defaultValue = "desc") String direction,
                                                  @RequestParam(required = false) String keyword,
                                                  @RequestParam("myuser") String myuser
    ){

        Sort sort=direction.equalsIgnoreCase("desc")?
                Sort.by(sortBy).descending():
                Sort.by(sortBy).ascending();

        Pageable sortPageable= PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sort
        );

        Page<UserConnectionDto> dto=collectionService.findRequestdUser(myuser,pageable);

        return ResponseEntity.ok(new ApiResponse(true, "Extracted", dto));

    }


    @CrossOrigin("*")
    @GetMapping("/received-user")
    public ResponseEntity<?> getReceivedRequestUser(
            @PageableDefault(page=0, size=10) Pageable pageable,
            @RequestParam(defaultValue="firstname") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String keyword,
            @RequestParam("myuser") String myuser
    ){
        Sort sort=direction.equalsIgnoreCase("desc")?
                Sort.by(sortBy).descending():
                Sort.by(sortBy).ascending();

        Pageable sortPageable= PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sort
        );

        Page<UserConnectionDto> dto=collectionService.findReceivedRequest(keyword,myuser,pageable);

        return ResponseEntity.ok(new ApiResponse(true, "Extracted", dto));


    }

    @CrossOrigin("*")
    @DeleteMapping("/cancelrequest")
    public ResponseEntity<?> cancelRequest(@RequestParam("userid") String userid, @RequestParam("myuser") String myuser){

        String result =collectionService.deleteRequest(userid, myuser);

        return ResponseEntity.ok(new ApiResponse(true, "", result));
    }

    @Transactional
    @Modifying
    @CrossOrigin("*")
    @PutMapping("/approverequest")
    public ResponseEntity<?> approveRequest(@RequestParam("userid") String userid, @RequestParam("myuser") String myuser){

        String result =collectionService.approveRequest(userid, myuser);

        return ResponseEntity.ok(new ApiResponse(true, "", result));
    }


    @CrossOrigin("*")
    @GetMapping("/connectedUser")
    public ResponseEntity<?> connecteduser(@RequestParam("myuser") String myuser,@PageableDefault(page=0, size=10) Pageable pageable){
        Page<UserConnectionDto> dto=collectionService.getConnectedUsers(myuser, pageable);

        return ResponseEntity.ok(new ApiResponse(true, "Extracted", dto));

    }


    @CrossOrigin("*")
    @GetMapping("/searchUser")
    public ResponseEntity<?> getUserByKeyword(@RequestParam("keyword") String keyword, String myuser, @PageableDefault(page=0, size=10) Pageable pageable)
    {
        Page<UserStatusDto> dto=collectionService.searchUserByKeyword(keyword, myuser, pageable);
        return ResponseEntity.ok(new ApiResponse(true, "Extracted", dto));
    }





}
