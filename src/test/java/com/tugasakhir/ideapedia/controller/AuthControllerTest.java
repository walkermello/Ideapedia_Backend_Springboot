package com.tugasakhir.ideapedia.controller;

import com.tugasakhir.ideapedia.utils.TokenGenerator;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthControllerTest {

    public static final String USER_NAME = "username123";
    public static final String PASSWORD = "password123";
    public static String AUTH_TOKEN;

    @BeforeClass
    public void doLoginTest() {
        // Jika token belum ada, lakukan login dan simpan token
        TokenGenerator tokenGenerator = new TokenGenerator(null);
        AUTH_TOKEN = tokenGenerator.getToken();
    }

    @Test
    public void testAuthTokenIsGenerated() {
        assert AUTH_TOKEN != null : "Token tidak berhasil di-generate";
        System.out.println("Token berhasil di-generate: " + AUTH_TOKEN);
    }
}
