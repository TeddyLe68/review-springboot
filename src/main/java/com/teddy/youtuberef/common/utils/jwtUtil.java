package com.teddy.youtuberef.common.utils;
import com.teddy.youtuberef.exception.AuthenticationException;
import io.jsonwebtoken.Jwts;

import com.teddy.youtuberef.entity.AccountEntity;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class jwtUtil {

    public static String generateToken(AccountEntity account, String jwtSecret, int jwtExpiration){
        return Jwts.builder()
                .setSubject(account.getUuid())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public static String getUserUuidFromJwtToken(String token, String jwtSecret){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public static void validateJwtToken(String token, String jwtSecret){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        }
        catch (Exception e) {
            throw new AuthenticationException("Invalid JWT token: " + e.getMessage());
        }
    }
}
