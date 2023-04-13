package jwt;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Base64;
import java.util.UUID;
import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class JWTAccount {

    private static final Key secret = MacProvider.generateKey(SignatureAlgorithm.HS256);
    private static final byte[] secretBytes = secret.getEncoded();
    // Note that in the code provided, the base64SecretBytes is apiKey.getSecret()
    private static final String base64SecretBytes = Base64.getEncoder().encodeToString(secretBytes);

    /**
     * The defined issuer for this JWTs
     */
    private static final String ISSUER = "secure-rest-api";

    /**
     * Create a JWT
     * @param subject identifies the principal that is the subject of the JWT (e.g. "user1")
     * @return the JWT
     */
    public static String createJWT(String subject) {
        //The JWT signature algorithm used to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //sign JWT with ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64SecretBytes);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        // Generate a random UUID
        String id = UUID.randomUUID().toString();

        //Set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setId(id) // is an optional claim that provides a unique identifier, UUID has very low probability of collision
                .setSubject(subject)
                .setIssuer(ISSUER)
                .claim("password", "password")
                .signWith(signatureAlgorithm, signingKey);

        // TODO: add these claims for the expiration date and time
        //.setIssuedAt(now)   // set the time when the token was issued
        //.setNotBefore(now)  // set the time before which the token is not yet valid (created)
        //.setExpiration(exp) // Expiration date

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    /**
     * Parse a JWT
     * @param jwt the JWT
     */
    public static void parseJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(base64SecretBytes))
                .parseClaimsJws(jwt).getBody();

        System.out.println("ID: " + claims.getId());
        System.out.println("Subject: " + claims.getSubject());
        System.out.println("Issuer: " + claims.getIssuer());
        System.out.println("Password: " + claims.get("password"));

        // TODO: add these claims for the expiration date and time
        //System.out.println("Issued At: " + claims.getIssuedAt()); // set the time when the token was issued
        //System.out.println("Not Before : "+claims.getNotBefore()); // set the time before which the token is not yet valid (created)
        //System.out.println("Expiration: " + claims.getExpiration()); // Expiration date
    }

    /**
     * Get a claim from a JWT
     * @param jwt the JWT to parse
     * @param claim the claim
     * @return the claim value
     */
    public static Object getClaim(String jwt, String claim) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(base64SecretBytes))
                .parseClaimsJws(jwt).getBody().get(claim);
    }
}
