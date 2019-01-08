package edu.knoldus.project.impl.repository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.time.DateUtils.addSeconds;

public class Authenticate {
    
    public static String createToken() {
        
        return Jwts.builder()
                .setSubject("")
                .signWith(HS512, new SecretKeySpec(parseBase64Binary("Bhawna"), HS512.getJcaName()))
                .setIssuedAt(new Date())
                .setExpiration(addSeconds(new Date(), 80))
                .compact();
    }
    
    public static String validateToken(String token) {
        
        if(isEmpty(token)) {
            
            throw new IllegalArgumentException();
        }
        
       try {
    
           return Jwts.parser()
                   .setSigningKey(new SecretKeySpec(parseBase64Binary("Bhawna"), HS512.getJcaName()))
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
    
       }
       catch(SignatureException ex) {
           
           throw new SignatureException(ex.getMessage());
       }
       
       catch (ExpiredJwtException ex) {
           
           throw new ExpiredJwtException(ex.getHeader(), ex.getClaims(), ex.getMessage());
       }
    }
}
