    package com.LostAndFound.UserService.service;

    import io.jsonwebtoken.Claims;
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.io.Decoders;
    import io.jsonwebtoken.security.Keys;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;

    import javax.crypto.SecretKey;
    import java.util.Date;
    import java.util.function.Function;

    @Service
    public class JwtService {

        @Value("${jwt.secret}")
        private String secretKey;
        public boolean validateToken(String token, String number)
        {
            String extractedPhoneNumber=extractNumber(token);
            return (extractedPhoneNumber.equals(number) && !isTokenExpired(token));

        }

        public String extractNumber(String token){
            return extractClaim(token, Claims::getSubject);
        }

        private Claims extractAllClaims(String token)
        {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }

        boolean  isTokenExpired(String token){
            return extractExpiration(token).before(new Date());
        }

        Date extractExpiration(String token)
        {
            return extractClaim(token, Claims::getExpiration);
        }

        private<T> T extractClaim(String token, Function<Claims,T> claimsResolver)
        {
            Claims claims=extractAllClaims(token);
            return claimsResolver.apply(claims);
        }
        SecretKey getKey() {
            byte[] keyBytes= Decoders.BASE64.decode(secretKey);
                return Keys.hmacShaKeyFor(keyBytes);

        }


    }
