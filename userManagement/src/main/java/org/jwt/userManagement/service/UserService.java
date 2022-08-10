package org.jwt.userManagement.service;

import org.jwt.userManagement.model.AppUser;
import org.jwt.userManagement.model.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    AppUser findByUsername(String username);
    List<AppUser> findAll();
    AppUser updateUser(AppUser appUser);
    AppUser saveUser(AppUser appUser);
    void deleteUser(Long id);
    void addUserRole(String username, String roleName);
    void updateAuthAttempts(Integer authAttempts, Long id);
    Role findRole(String name);
}
