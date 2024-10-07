package library.management.security;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.PasswordAuthentication;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static io.vavr.control.Try.ofCallable;

@Log4j2
@Service
public class JwtService {

	private static final String SECRET_KEY = "VGhlIEphdmEgRGV2IEhlcmU=";
	private static final int EXPIRATION_TIME_MILLIS = 1000 * 20;

	private final SecretKey secretKey;
	private final JwtParser jwtParser;

	public JwtService() {
		MessageDigest messageDigest = ofCallable(() -> MessageDigest.getInstance("SHA-512")).get();
		secretKey = new SecretKeySpec(messageDigest.digest(SECRET_KEY.getBytes()), "HmacSHA512");
		jwtParser = Jwts.parser()
				.verifyWith(secretKey)
				.requireIssuer("user")
				.requireSubject("jwt-token")
				.build();
	}


	public String generateToken(PasswordAuthentication authentication, Map<String, ?> claims) {
		return Jwts.builder()
				.issuer("user")
				.claims(claims)
				.subject("jwt-token")
				.claim("username", authentication.getUserName())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS))
				.id(UUID.randomUUID().toString())
				.signWith(secretKey, Jwts.SIG.HS512)
				.compact();
	}

	public boolean isValid(String token) {
		try {
			jwtParser.parse(token);
			return true;
		} catch (Exception exception) {
			log.error("Invalid token", exception);
			return false;
		}
	}

	public Claims validateToken(String token) {
		try {
			return extractAllClaims(token);
		} catch (Exception exception) {
			log.error("Invalid token", exception);
			return null;
		}
	}

	private boolean isExpired(String token) {
		Date date = extractClaim(token, Claims::getExpiration);
		log.info("Expiration date: {}", date);
		return date.before(new Date());
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return jwtParser.parseSignedClaims(token).getPayload();
	}

}
