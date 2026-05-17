package com.skillswap.userrepository;

import com.skillswap.userdto.UserStatusDto;
import com.skillswap.userentity.Connection;
import com.skillswap.userentity.UserEntiity;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, String> {

    @Query("""
select new com.skillswap.userdto.UserStatusDto
(
  u.firstname,
  u.lastname,
  u.bio,
  u.userid,
  c.isConnected
)

from UserEntiity u 
LEFT JOIN Connection c

ON(
 ( c.sender.id=:myuser and c.receiver.id=u.id) OR
 ( c.sender.id=u.id and c.receiver.id=:myuser)
)

where u.id!=:myuser

AND
(
LOWER(u.firstname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR 
LOWER(u.lastname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR 
LOWER(CONCAT(u.firstname,' ',u.lastname)) LIKE LOWER(CONCAT('%', :keyword, '%'))
)







""")
    Page<UserStatusDto> searchUserByKeyword(@Param("keyword") String keyword, @Param("myuser") String myuser, Pageable pageable);

    @Query(
            """
select u from UserEntiity u where 
u.id!=:myuser

AND u.id NOT IN (
    SELECT 
        CASE 
            WHEN c.sender.id = :myuser THEN c.receiver.id
            ELSE c.sender.id
        END
    FROM Connection c
    WHERE 
        c.sender.id = :myuser 
        OR c.receiver.id = :myuser
)


"""
    )
    Page<UserEntiity> searchUser(@Param("myuser") String myuser, Pageable pageable);


    @Query(
            """
select u FROM UserEntiity u
WHERE 
u.id!=:myuser


AND
u.id IN (
 select c.receiver.id
    
     FROM Connection c
     WHERE
     c.sender.id=:myuser  and
     c.isConnected= 'N'
     
)



"""
    )
    Page<UserEntiity> searchRequestedUser(@Param("myuser") String myuser, Pageable pageable);


    @Transactional
    @Modifying
    @Query(
            """
Delete FROM Connection c where (c.receiver.id=:receiver AND
c.sender.id=:sender) OR
 (c.receiver.id=:sender AND
c.sender.id=:receiver)

"""
    )
    public int deleteByReceiverAndSender(@Param("sender") String  sender, @Param("receiver") String receiver);


    @Transactional
    @Modifying
    @Query(
            """
update Connection c 
set c.isConnected='Y'
where c.receiver.id=:receiver AND c.sender.id=:sender

"""
    )
    public int updateConnectionStatus(@Param("sender") String sender, @Param("receiver") String receiver);


    @Query(
            """

                    SELECT u FROM UserEntiity u
WHERE u.id != :myuser
AND u.id IN (
    SELECT\s
        CASE\s
            WHEN c.sender.id = :myuser THEN c.receiver.id
            ELSE c.sender.id
        END
    FROM Connection c
    WHERE\s
        (c.sender.id = :myuser OR c.receiver.id = :myuser)
        AND c.isConnected = 'Y'

    
    
)
"""
    )
    Page<UserEntiity> getConnectedUser(@Param("myuser") String myuser, Pageable pageablr);


    @Query(
            """
select u from UserEntiity u
where 
u.id!=:myuser

AND
u.id in (
SELECT c.sender.id
from Connection c
where 
c.receiver.id=:myuser
AND
c.isConnected='N'

)


"""
    )
    Page<UserEntiity> getReceivedRequests(@Param("myuser") String myuser, Pageable pageable);

    @Query("""
SELECT COUNT(c) from Connection c
where c.receiver.id=:userid AND
c.isConnected="N"
""")
    int getCountOfRequestReceived(@Param("userid") String userid);




}
