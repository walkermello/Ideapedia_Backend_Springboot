package com.tugasakhir.ideapedia.config;

import com.tugasakhir.ideapedia.security.Crypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:jwt.properties")
public class JwtConfig {

    private static String jwtExpiration;
    private static String jwtSecret;


    public static String getJwtExpiration() {
        return jwtExpiration;
    }

    @Value("${jwt.expiration}")
    private void setJwtExpiration(String jwtExpiration) {
        JwtConfig.jwtExpiration = jwtExpiration;
    }

    public static String getJwtSecret() {
        return jwtSecret;
    }

    @Value("${jwt.secret}")
    private void setJwtSecret(String jwtSecret) {
        this.jwtSecret = Crypto.performDecrypt(jwtSecret);

    }
}