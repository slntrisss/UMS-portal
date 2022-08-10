package org.jwt.userManagement.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jwt.userManagement.auth.AppUserPermission;
import org.jwt.userManagement.enumeration.AccountStatus;
import org.jwt.userManagement.enumeration.Status;
import org.jwt.userManagement.model.AppUser;
import org.jwt.userManagement.model.Role;
import org.jwt.userManagement.repository.RoleRepo;
import org.jwt.userManagement.repository.UserRepo;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.jwt.userManagement.auth.AppUserPermission.USER_READ;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private RoleRepo roleRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp(){
        userService = new UserServiceImpl(userRepo, roleRepo, passwordEncoder);
    }

    @Test
    void findByUsername() {
        String username = "tom";
        AppUser appUser = Mockito.mock(AppUser.class);
        Mockito.when(userRepo.findByUsername(username)).thenReturn(appUser);

        assertNotNull(userService.findByUsername(username));
    }

    @Test
    void WillThrowWhenUsernameNotFound(){
        String username = "qwety";

        assertThatThrownBy(() -> userService.findByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(String.format("User by username %s not found", username));
    }

    @Test
    void findAll() {
        userService.findAll();
        Mockito.verify(userRepo).findAll();
    }

    @Test
    void updateUser() {
        Role userRole = new Role(null, "USER", new ArrayList<AppUserPermission>(){{
            add(USER_READ);
        }});
        byte[] tomImg = getPicInBytes("/Users/raiymbekmerekeyev/Desktop/tom.png");
        AppUser user = new AppUser(null, "Tom", "Holland",
                "tom@gmail.com", "tom", tomImg, "", "password", Status.ACTIVE,0,
                AccountStatus.NON_LOCKED, userRole);
        userService.updateUser(user);
        Mockito.verify(userRepo).save(user);
    }

    @Test
    void saveUser() {
        Role userRole = new Role(null, "USER", new ArrayList<AppUserPermission>(){{
            add(USER_READ);
        }});
        byte[] tomImg = getPicInBytes("/Users/raiymbekmerekeyev/Desktop/tom.png");
        AppUser user = new AppUser(null, "Tom", "Holland",
                "tom@gmail.com", "tom", tomImg, "", "password", Status.ACTIVE,0,
                AccountStatus.NON_LOCKED, userRole);

        userService.saveUser(user);
        ArgumentCaptor<AppUser> argumentCaptor =
                ArgumentCaptor.forClass(AppUser.class);
        Mockito.verify(userRepo).save(argumentCaptor.capture());

        AppUser capturedUser = argumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(user);
    }

    @Test
    void deleteUser() {
        userService.deleteUser(5L);
        Mockito.verify(userRepo).deleteById(5L);
    }

    @Test
    void addUserRole() {
        String username = "tom";
        String roleName = "USER";

        AppUser user = Mockito.mock(AppUser.class);
        Role role = Mockito.mock(Role.class);
        ArgumentCaptor<String> argumentCaptor1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> argumentCaptor2 = ArgumentCaptor.forClass(String.class);

        Mockito.when(roleRepo.findByName(roleName)).thenReturn(role);
        Mockito.when(userRepo.findByUsername(username)).thenReturn(user);

        userService.addUserRole(username, roleName);
        Mockito.verify(userRepo).findByUsername(argumentCaptor1.capture());
        Mockito.verify(roleRepo).findByName(argumentCaptor2.capture());

        assertThat(username).isEqualTo(argumentCaptor1.getValue());
        assertThat(roleName).isEqualTo(argumentCaptor2.getValue());
    }

    @Test
    void updateAuthAttempts() {
        userService.updateAuthAttempts(3, 5L);
        Mockito.verify(userRepo).updateAuthAttempts(3, 5L);
    }

    @Test
    void loadUserByUsername(){
        String username = "tom";
        AppUser user = Mockito.mock(AppUser.class);
        Mockito.when(userRepo.findByUsername(Mockito.anyString())).thenReturn(user);

        UserDetails userDetails = userService.loadUserByUsername(username);

        Assertions.assertNotNull(userDetails);
    }
    private byte[] getPicInBytes(String path){
        File file = new File(path);
        byte[] picInBytes = new byte[(int)file.length()];
        try(FileInputStream fileInputStream = new FileInputStream(file)){
            fileInputStream.read(picInBytes);
        }catch (IOException exc){
            exc.getStackTrace();
        }
        return picInBytes;
    }
}