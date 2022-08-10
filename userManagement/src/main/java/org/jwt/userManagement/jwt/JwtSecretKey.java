package org.jwt.userManagement.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Random;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
public class JwtSecretKey {

    @Bean
    public SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(generateSecretKey().getBytes(UTF_8));
    }
    private String generateSecretKey(){
        int seed = Integer.MIN_VALUE + (int)(Math.random()*Integer.MAX_VALUE);
        Random random = new Random(12);
        char[] arr = new char[100];
        for(int i = 0; i < arr.length; i++){
            arr[i] = (char)random.nextInt(256);
        }
        return new String(arr);
    }
}
