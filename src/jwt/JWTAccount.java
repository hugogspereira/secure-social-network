package jwt;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class JWTAccount {

    /**
     * The defined issuer for this JWTs
     */
    private static final String ISSUER = "secure-rest-api";
    /**
     * The secret bytes used to sign the token
     */
    private String base64SecretBytes;

    /**
     * The singleton instance
     */
    private static JWTAccount instance;

    public static JWTAccount getInstance() {
        if(instance == null) {
            instance = new JWTAccount();
        }
        return instance;
    }

    public JWTAccount() {
        Key secret = MacProvider.generateKey(SignatureAlgorithm.HS256);
        byte[] secretBytes = secret.getEncoded();
        // Note that in the code provided, the base64SecretBytes is apiKey.getSecret()
        base64SecretBytes = Base64.getEncoder().encodeToString(secretBytes);
    }

    /**
     * Create a JWT
     * @param subject identifies the principal that is the subject of the JWT (e.g. "user1")
     * @return the JWT
     */
    public String createJWT(String subject, String password) {
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
                .setExpiration(new Date(System.currentTimeMillis() + 100000)) // 100 seconds
                .setNotBefore(new Date(System.currentTimeMillis()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .claim("accountName", subject)
                .claim("password", password)
                .signWith(signatureAlgorithm, signingKey);

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    /**
     * Parse a JWT
     * @param jwt the JWT
     */
    public void parseJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(base64SecretBytes))
                .parseClaimsJws(jwt).getBody();

        System.out.println("ID: " + claims.getId());
        System.out.println("Subject: " + claims.getSubject());
        System.out.println("Issuer: " + claims.getIssuer());
        System.out.println("Password: " + claims.get("password"));
        System.out.println("Issued At: " + claims.getIssuedAt()); // set the time when the token was issued
        System.out.println("Not Before : "+claims.getNotBefore()); // set the time before which the token is not yet valid (created)
        System.out.println("Expiration: " + claims.getExpiration()); // Expiration date
    }

    /**
     * Get a claim from a JWT
     * @param jwt the JWT to parse
     * @param claim the claim
     * @return the claim value
     */
    public Object getClaim(String jwt, String claim) throws ExpiredJwtException {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(base64SecretBytes))
                .parseClaimsJws(jwt).getBody().get(claim);
    }
}
