package org.microservices.auth;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

/**
 * @author xiangqian
 * @date 23:08 2022/06/23
 */
public class ClientSecretTest {

    @Test
    public void test() throws IOException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode("123456");
        System.out.format("encryptedPassword: %s", encryptedPassword).println();
    }


}
