package org.jwt.userManagement.repository;

import org.jwt.userManagement.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<AppUser, Long> {

    AppUser findByUsername(String username);

    @Modifying
    @Query("update AppUser user set user.authAttempts = :authAttempts where user.id = :id")
    void updateAuthAttempts(@Param(value = "authAttempts") Integer authAttempts, @Param(value = "id") Long id);
}
