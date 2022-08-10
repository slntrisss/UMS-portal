package org.jwt.userManagement.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@ConfigurationProperties(prefix = "authorization.jwt")
@Data
@NoArgsConstructor
@Component
public class JwtConfig {
    private String prefix;
    private int tokenExpirationDays;
    public String getAuthorizationHeader(){
        return AUTHORIZATION;
    }
}
