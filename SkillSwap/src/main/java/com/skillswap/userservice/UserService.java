package com.skillswap.userservice;

import com.skillswap.userdto.RegisterDto;
import com.skillswap.userdto.UserProfileDto;

public interface UserService {

    public boolean registerUser(RegisterDto dto);

    public UserProfileDto getUserDetails(String userid);


}
