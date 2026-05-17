package com.skillswap.usercontroller;


import com.skillswap.SpringSecurity.JwtUtil;
import com.skillswap.userdto.DashBoardDto;
import com.skillswap.userdto.LoginDto;
import com.skillswap.userdto.RegisterDto;
import com.skillswap.userdto.UserProfileDto;
import com.skillswap.userservice.ServiceImpl;
import com.skillswap.userservice.UserSkillsServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;

@RestController
public class UserController {

    ServiceImpl service;
    JwtUtil jwtUtil;
    AuthenticationManager authManager;
    UserSkillsServiceImpl userSkillService;

    public UserController(ServiceImpl service, JwtUtil jwtUtil, AuthenticationManager authManager, UserSkillsServiceImpl userSkillService){
      this.service=service;
      this.jwtUtil=jwtUtil;
      this.authManager=authManager;
      this.userSkillService=userSkillService;
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("TEST HIT");
        return "OK";
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/register")
    public ResponseEntity<?> registeUser(@Valid @RequestBody RegisterDto dto, BindingResult result) throws Exception{
        HashMap<String, String> map=new HashMap<>();

        if(result.hasErrors()){
            System.out.println("Errors: " + result.getAllErrors());
            for(FieldError error: result.getFieldErrors()){
                map.put(error.getField(), error.getDefaultMessage());

            }
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, "Validation failed", map)
            );
        }
        else{

            String response=service.registerUser(dto);


            if(response.equals("SUCCESS")){
                return ResponseEntity.ok(new ApiResponse(true,"User Registered Sucessfully", dto));
            }
            else{
                return ResponseEntity.badRequest().body(
                        new ApiResponse(false, response, null)
                );
            }
        }

    }

    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginDto dto, BindingResult result, HttpServletResponse response){
        HashMap<String, String> map=new HashMap<>();
        if(result.hasErrors()){
            for(FieldError error: result.getFieldErrors()){
                map.put(error.getField(), error.getDefaultMessage());
            }
        }
        else{
            UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(dto.getUserid(),dto.getPassword());

            Authentication authentication=authManager.authenticate(token);

            if(authentication.isAuthenticated()){
                String jwtToken=jwtUtil.generateToken(dto.getUserid());

                Cookie cookie=new Cookie("JWT", jwtToken);
                cookie.setHttpOnly(true);
                cookie.setMaxAge(30*60);
                cookie.setSecure(false);
                cookie.setPath("/");

                response.addCookie(cookie);

                response.setHeader("Authorization","Bearer "+jwtToken);

                return ResponseEntity.ok(new ApiResponse(true, "token="+jwtToken,dto));





            }

            return ResponseEntity.badRequest().body(new ApiResponse(false,"User Log in fail", null));




        }

        return ResponseEntity.badRequest().body(new ApiResponse(false,"User Log in fail", null));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/profile")
    public ResponseEntity<?> userDetails(@RequestParam String userid, Principal principal){

        String currentuser=principal.getName();
        System.out.println(currentuser);
        boolean isCurrentUser=currentuser.equals(userid);

        UserProfileDto dto=service.getUserDetails(userid);
        dto.setCurrentUser(isCurrentUser);
        return ResponseEntity.ok(new ApiResponse(true, "User details are extracted", dto));



    }

    @CrossOrigin(origins = "*")
    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(){
        return ResponseEntity.ok(new ApiResponse(true, "valid token", null));

    }

    @CrossOrigin(origins = "*")
    @PostMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody UserProfileDto dto){
        try{
            UserProfileDto updatedProdile=service.updateProfile(dto);
            return ResponseEntity.ok(new ApiResponse(true,"updated", updatedProdile));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse(false,"Update fail", null));
        }

    }

    @Transactional
    @Modifying
    @CrossOrigin("*")
    @PutMapping("/updateImage")
    public ResponseEntity<?> updateProfileImage(@RequestParam("image")MultipartFile image, Principal principal) throws IOException {

        String userid=principal.getName();
        String result=service.updateImage(userid, image);

        return ResponseEntity.ok(new ApiResponse(true, result, null));



    }

    @CrossOrigin("*")
    @GetMapping("/getProfileImage")
    public ResponseEntity<?> getProfileImage(@RequestParam String userid, Principal principal){

     byte[] image=service.getProfileImage(userid);


        return ResponseEntity.ok()

                .contentType(
                        MediaType.IMAGE_JPEG
                )

                .body(image);


    }

    @CrossOrigin("*")
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashBoardDetails(@RequestParam String userid){

        DashBoardDto dto= userSkillService.getDetailsForDashboard(userid);

        return ResponseEntity.ok(new ApiResponse(true, "extracted", dto));ll

    }


}
