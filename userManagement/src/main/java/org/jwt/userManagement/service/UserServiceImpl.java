package org.jwt.userManagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jwt.userManagement.model.AppUser;
import org.jwt.userManagement.model.Role;
import org.jwt.userManagement.repository.RoleRepo;
import org.jwt.userManagement.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser findByUsername(String username) {
        AppUser user = userRepo.findByUsername(username);
        if(user == null){
            log.error("User by username {} not found", username);
            throw new UsernameNotFoundException(String.format("User by username %s not found", username));
        }
        log.info("User by username {} retrieved", username);
        return user;
    }

    @Override
    public List<AppUser> findAll() {
        log.info("Users retrieved");
        return userRepo.findAll();
    }

    @Override
    public AppUser updateUser(AppUser appUser) {
        log.info("User by username {} updated", appUser.getUsername());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        Role role = roleRepo.findByName(appUser.getRole().getName());
        appUser.setRole(role);
        if(appUser.isAccountNonLocked())
            appUser.setAuthAttempts(0);
        if(appUser.getProfileImageInBase64() == null){
            AppUser user = userRepo.findByUsername(appUser.getUsername());
            appUser.setProfileImage(user.getProfileImage());
        }
        else
            convertBase64ToImage(appUser);
        return userRepo.save(appUser);
    }

    @Override
    public AppUser saveUser(AppUser appUser) {
        log.info("User by username {} saved", appUser.getUsername());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        Role role = roleRepo.findByName(appUser.getRole().getName());
        appUser.setRole(role);
        if(appUser.getProfileImageInBase64() == null)
            setProfilePhotoIfNotExists(appUser);
        else
            convertBase64ToImage(appUser);
        return userRepo.save(appUser);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("User by id {} deleted", id);
        userRepo.deleteById(id);
    }

    @Override
    public void addUserRole(String username, String roleName) {
        log.info("Adding a role {} to user by username {}", roleName, username);
        AppUser appUser = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        appUser.setRole(role);
        userRepo.save(appUser);
    }

    @Override
    public void updateAuthAttempts(Integer authAttempts, Long id) {
        log.info("Updating failed attempts count");
        userRepo.updateAuthAttempts(authAttempts, id);
    }

    @Override
    public Role findRole(String roleName) {
        log.info("User role retrieved");
        return roleRepo.findByName(roleName);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails appUser = userRepo.findByUsername(username);
        if(appUser == null){
            log.error("User by username {} not found", username);
            throw new UsernameNotFoundException(String.format("User by username %s not found", username));
        }
        log.info("User by username {} retrieved", username);
        return appUser;
    }

    private void setProfilePhotoIfNotExists(AppUser appUser){
        File file = new File("src/main/resources/image/defaultProfileImg.jpg");
        byte[] defaultProfileImg = new byte[(int) file.length()];
        try(FileInputStream fileInputStream = new FileInputStream(file)){
            fileInputStream.read(defaultProfileImg);
        }catch (IOException exc){
            exc.getStackTrace();
        }
        appUser.setProfileImage(defaultProfileImg);
    }

    private void convertBase64ToImage(AppUser appUser){
        String base64 = appUser.getProfileImageInBase64().split(",")[1];
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64);
        appUser.setProfileImage(imageBytes);
    }
}
