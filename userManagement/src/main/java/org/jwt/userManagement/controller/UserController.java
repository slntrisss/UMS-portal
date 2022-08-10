package org.jwt.userManagement.controller;

import lombok.RequiredArgsConstructor;
import org.jwt.userManagement.model.AppUser;
import org.jwt.userManagement.model.Response;
import org.jwt.userManagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<Response> getAll(){
        return ResponseEntity.ok(
                Response.builder()
                        .message("Users retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .data(new HashMap<String, List<AppUser>>(){{
                            put("users", userService.findAll());
                        }})
                        .build()
        );
    }

    @GetMapping("/{username}")
    public ResponseEntity<Response> getUser(@PathVariable("username") String username){
        return ResponseEntity.ok(
                Response.builder()
                        .status(OK)
                        .statusCode(OK.value())
                        .message("User retrieved")
                        .data(new HashMap<String, AppUser>(){{
                            put("user", userService.findByUsername(username));
                        }})
                        .build()
        );
    }
}
