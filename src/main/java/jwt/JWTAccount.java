package jwt;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Base64;
import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class JWTAccount {

    private static final Key secret = MacProvider.generateKey(SignatureAlgorithm.HS256);
    private static final byte[] secretBytes = secret.getEncoded();
    // Note that in the code provided, the base64SecretBytes is apiKey.getSecret()
    private static final String base64SecretBytes = Base64.getEncoder().encodeToString(secretBytes);

    public static String createJWT(String id, String issuer, String subject) {
        //The JWT signature algorithm used to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //sign JWT with ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64SecretBytes);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setSubject(subject)
                .setIssuer(issuer)
                //.setIssuedAt(now)   // set the time when the token was issued
                //.setNotBefore(now)  // set the time before which the token is not yet valid (created)
                //.setExpiration(exp) // Expiration date
                .signWith(signatureAlgorithm, signingKey);
        // Note that we can use .set(VARIABLE_NAME, var)

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public static void parseJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(base64SecretBytes))
                .parseClaimsJws(jwt).getBody();

        // Note that we can use claims.get(VARIABLE_NAME)

        System.out.println("ID: " + claims.getId());
        System.out.println("Subject: " + claims.getSubject());
        System.out.println("Issuer: " + claims.getIssuer());
        //System.out.println("Issued At: " + claims.getIssuedAt()); // set the time when the token was issued
        //System.out.println("Not Before : "+claims.getNotBefore()); // set the time before which the token is not yet valid (created)
        //System.out.println("Expiration: " + claims.getExpiration()); // Expiration date
    }
}
