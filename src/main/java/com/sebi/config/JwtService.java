package com.sebi.config;

import com.sebi.model.Token;
import com.sebi.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;
import com.sebi.model.User;
@AllArgsConstructor
@Service
public class JwtService {

    private final TokenRepository tokenRepository;

    public <T> T extractClaim(String token , Function<Claims,T> resolver){
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }
    public String extractBearer(String jwt)
    {
        jwt=jwt.substring(7);
        return jwt;
    }

    public boolean isValid(String token, UserDetails user)
    {
        String username = extractUserName(token);
        Optional<Token> tokenFound = tokenRepository.findByToken(token);
        if(tokenFound.isPresent())
        {
            return (username.equals(user.getUsername())) && !isTokenExpired(token) && !tokenFound.get().isLoggedOut();
        }else {
            return (username.equals(user.getUsername())) && !isTokenExpired(token);
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());

    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    public String extractUserName(String token){
        return extractClaim(token,Claims::getSubject);
    }

    private Claims extractAllClaims(String token) { //verifica tokenul cu cheia semnata si extrage tot
        return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String generateToken(User user){ //genereaza un token cu email,dataEmitere, dataExpirare, semneaza cu cheia
        String token = Jwts
                .builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24*60*60*1000))
                .signWith(getSigninKey())
                .compact();
        return token;
    }

    private SecretKey getSigninKey(){
        String SECRET_KEY = "9c1812ea8a6bf9f7e99cc9cc4cbca63408744421c0068b7a6f31c49bb6c53033";
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);

    }
}
