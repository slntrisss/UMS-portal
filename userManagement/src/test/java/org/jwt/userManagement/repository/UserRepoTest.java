package org.jwt.userManagement.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.jwt.userManagement.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @Test
    void contextLoads(){
        Assertions.assertNotNull(userRepo);
    }

    @Test
    void updateAuthAttemptsTest(){
        AppUser user = userRepo.findByUsername("tom");
        user.setAuthAttempts(10);
        userRepo.updateAuthAttempts(user.getAuthAttempts(), user.getId());
        AppUser updatedUser = userRepo.findByUsername("tom");
        assertThat(updatedUser.getAuthAttempts()).isEqualTo(10);
    }
}