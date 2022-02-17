package com.socialNetwork.security.jwt;

import com.socialNetwork.exceptions.UserFriendlyException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationTimeInSeconds}")
    private Long expirationTimeInSeconds;

    /**
     * Generate token by login
     * WARNING: Expiration time calculating in UTC
     * @param login - user login
     * @return - jwt token
     */
    public String generateToken(String login){
        Date date = Date.from(LocalDateTime.now().plusSeconds(expirationTimeInSeconds).toInstant(ZoneOffset.of("+00:00")));
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) throws UserFriendlyException {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            throw new UserFriendlyException( "Token expired");
        } catch (UnsupportedJwtException unsEx) {
            throw new UserFriendlyException("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            throw new UserFriendlyException("Malformed jwt");
        } catch (SignatureException sEx) {
            throw new UserFriendlyException("Invalid signature");
        } catch (Exception e) {
            throw new UserFriendlyException("invalid token");
        }
    }

    public String getLoginFromToken(String token){
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
