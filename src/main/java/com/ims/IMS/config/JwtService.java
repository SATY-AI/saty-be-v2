package com.ims.IMS.config;
import com.ims.IMS.common.Role;
import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.model.groupuser.Customer;
import com.ims.IMS.model.groupuser.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "cf65349058abf928590c9daf21d91ae49c081ada8698b99146153adb49c93397";

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public String generateToken(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("email", user.getEmail());
        extraClaims.put("role", user.getRole());
        extraClaims.put("fullName", user.getFullName());

        return generateToken(extraClaims, user);
    }

    public String generateTokenCustomer(Customer customer) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("email", customer.getEmail());
        extraClaims.put("role", customer.getRole());
        extraClaims.put("fullName", customer.getFullName());
        extraClaims.put("secretKey", customer.getSecret_key());
        return generateToken(extraClaims, customer);
    }

    public String generateTokenAdmin(Admin admin) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("email", admin.getEmail());
        extraClaims.put("role", admin.getRole());
        extraClaims.put("fullName", admin.getFullName());

        return generateToken(extraClaims, admin);
    }

    private String generateToken (
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 1000 * 120))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()));
        //!isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractToken(String tokenHeader) {
        // Check if the header is not null and starts with "Bearer "
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            return tokenHeader.substring(7); // Remove "Bearer " to get the token
        }
        return null; // Invalid token header
    }

    public Customer decodeTokenCustomer(String jwtToken) {
        try {
            // Decode the JWT token to retrieve user details
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignInKey()) // Use your secret key
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();

            // Extract user details from claims
            String fullname = claims.get("fullName", String.class);
            String email = claims.get("email", String.class);
            String roleString = claims.get("role", String.class); // Extract role as a String

            // Convert the role String back to the Role enum
            Role role = Role.valueOf(roleString);

            // Create a User object with extracted details
            Customer customer = new Customer();
            customer.setFullName(fullname);
            customer.setEmail(email);
            customer.setRole(role); // Set the role in the User object

            return customer;
        } catch (Exception e) {
            // Token decoding or verification failed
            throw new RuntimeException("Invalid token Customer");
        }
    }

    public Admin decodeTokenAdmin(String jwtToken) {
        try {
            // Decode the JWT token to retrieve user details
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignInKey()) // Use your secret key
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
            // Extract user details from claims
            String fullname = claims.get("fullName", String.class);
            String email = claims.get("email", String.class);
            String roleString = claims.get("role", String.class); // Extract role as a String
            // Convert the role String back to the Role enum
            Role role = Role.valueOf(roleString);
            // Create a User object with extracted details
            Admin admin = new Admin();
            admin.setFullName(fullname);
            admin.setEmail(email);
            admin.setRole(role); // Set the role in the User object
            return admin;
        } catch (Exception e) {
            // Token decoding or verification failed
            throw new RuntimeException("Invalid Admin Token");
        }
    }


}
