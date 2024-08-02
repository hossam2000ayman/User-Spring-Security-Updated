package com.example.springsecurityjwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

public class JwtSecretMaker {
    @Test
    public void generateSecretKey() {
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String encodedKey = DatatypeConverter.printHexBinary(secretKey.getEncoded());
        System.out.println("Key = [" + encodedKey + "] ");
    }
}
