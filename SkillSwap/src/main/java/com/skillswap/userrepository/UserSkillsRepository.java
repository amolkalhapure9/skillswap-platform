package com.skillswap.userrepository;

import com.skillswap.userentity.UserSkills;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSkillsRepository extends JpaRepository<UserSkills, Integer> {

  @Query("Select s from UserSkills s where s.user.id=:userid and s.category=:category")
  List<UserSkills> findAllByUser_Id(@Param("userid")String userid, @Param("category") String category);

  @Modifying
  @Transactional
  @Query("delete from UserSkills s where s.user.id=:userid and s.skill.id=:skillid and s.category=:category")
  void deleteByUser_Id(@Param("userid") String userid, @Param("skillid") int Skillid, @Param("category") String category);


  @Query("""
    SELECT COUNT(s) from UserSkills s where s.user.id=:userid and s.category="KNOW"
    
""")
  int getCountofKnowSkills(@Param("userid") String userid);



}
