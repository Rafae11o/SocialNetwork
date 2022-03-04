package com.socialNetwork.security.jwt;

import com.socialNetwork.dto.TokenInfo;
import com.socialNetwork.exceptions.UserFriendlyException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    private static Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationTimeInSeconds}")
    private Long expirationTimeInSeconds;

    @Value("${dev-mode.expirationTimeInSeconds}")
    private Long expirationTimeInSecondsForDevMode;

    @Value("${dev-mode.enable}")
    private Boolean devMode;

    /**
     * Generate token by login
     * WARNING: Expiration time calculating in UTC
     * @param login - user login
     * @return - jwt token
     */
    public TokenInfo generateToken(String login){
        Long expirationTimeInSeconds = devMode ? expirationTimeInSecondsForDevMode : this.expirationTimeInSeconds;
        Date date = Date.from(LocalDateTime.now().plusSeconds(expirationTimeInSeconds).atZone(ZoneId.systemDefault()).toInstant());
        logger.info("Expiration date: {}, expiration time in seconds: {}", date, expirationTimeInSeconds);
        String token =  Jwts.builder()
                .setSubject(login)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        return new TokenInfo(token, date);
    }

    public boolean validateToken(String token) throws UserFriendlyException {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            logger.error("Token expired");
            throw new UserFriendlyException( "Token expired");
        } catch (UnsupportedJwtException unsEx) {
            logger.error("Unsupported jwt");
            throw new UserFriendlyException("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            logger.error("Malformed jwt");
            throw new UserFriendlyException("Malformed jwt");
        } catch (SignatureException sEx) {
            logger.error("Invalid signature");
            throw new UserFriendlyException("Invalid signature");
        } catch (Exception e) {
            logger.error("Invalid token");
            throw new UserFriendlyException("Invalid token");
        }
    }

    public String getLoginFromToken(String token){
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
