package org.jwt.userManagement.controller;

import lombok.RequiredArgsConstructor;
import org.jwt.userManagement.model.AppUser;
import org.jwt.userManagement.model.Role;
import org.jwt.userManagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import org.jwt.userManagement.model.Response;

@RestController
@RequestMapping("management/api/user")
@RequiredArgsConstructor
public class UserManagementController {

    private final UserService userService;

    @PostMapping("/save")
    public ResponseEntity<Response> save(@RequestBody AppUser user){
        return ResponseEntity.ok(
                Response.builder()
                        .message("User saved")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .data(new HashMap<String, AppUser>(){{
                            put("user", userService.saveUser(user));
                        }})
                        .build()
        );
    }

    @GetMapping("/get-user-role/{roleName}")
    public ResponseEntity<Role> getUserRole(@PathVariable("roleName") String roleName){
        return new ResponseEntity<>(userService.findRole(roleName), OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Response> update(@RequestBody AppUser appUser){
        return ResponseEntity.ok(
                Response.builder()
                        .message("User updated")
                        .statusCode(OK.value())
                        .status(OK)
                        .data(new HashMap<String, AppUser>(){{
                            put("user", userService.updateUser(appUser));
                        }})
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(
                Response.builder()
                        .message(String.format("User by id %d deleted", id))
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}
