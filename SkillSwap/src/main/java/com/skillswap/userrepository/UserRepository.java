package com.skillswap.userrepository;

import com.skillswap.userentity.UserEntiity;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntiity, String> {

    public UserEntiity findByUserid(String userid);

    public UserEntiity findByEmail(String email);





}




