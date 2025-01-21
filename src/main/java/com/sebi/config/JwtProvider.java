package com.sebi.config;

import com.sebi.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
public class JwtProvider {
    SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
    public String generateToken(Authentication auth){


        String jwt= Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+846000000))
                .claim("email",auth.getName())
                .claim("role",auth.getAuthorities())
                .signWith(key).compact();
        return jwt;
    }
//    public String generateTokenForMe(User user){
//        String jwt= Jwts.builder()
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(new Date().getTime()+846000000))
//                .claim("email",user.getEmail())
//                .signWith(key).compact();
//        return jwt;
//    }

    public String getEmailFromToken(String jwt)
    {
        jwt=jwt.substring(7);
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

        String email=String.valueOf(claims.get("email"));
        return email;
    }
}
