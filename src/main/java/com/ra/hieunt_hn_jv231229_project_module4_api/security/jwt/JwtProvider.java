package com.ra.hieunt_hn_jv231229_project_module4_api.security.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider
{
    @Value("${jwt_expiration}")
    private Integer expiration;
    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();
//    private final String base64Key = Encoders.BASE64.encode(secretKey.getEncoded());

    public String createToken(UserDetails userDetails)
    {
        Date today = new Date();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(today)
                .expiration(new Date(today.getTime() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token)
    {
        try
        {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (UnsupportedJwtException e)
        {
            log.error("Unsupported JWT Exception {}", e.getMessage());
        } catch (JwtException e)
        {
            log.error("JWT Exception {}", e.getMessage());
        } catch (IllegalArgumentException e)
        {
            log.error("Illegel Argument Exception {}", e.getMessage());
        }
        return false;
    }

    public String getUserNameFromToken(String token)
    {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getSubject();
    }
}
