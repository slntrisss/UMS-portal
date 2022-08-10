package org.jwt.userManagement.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsernameAndPasswordRequest {
    private String username;
    private String password;
}
