package gov.nist.csd.pm.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenHelper {
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

	private static final String secret =
			"569e58bcf79c476ea517c2ec9faef9fd569e58bcf79c476ea517c2ec9faef9fd569e58bcf79c476ea517c2ec9faef9fd";

	private static JwtTokenHelper mJwtTokenHelper;

	public static JwtTokenHelper getInstance() {
		if(null == mJwtTokenHelper)
			mJwtTokenHelper = new JwtTokenHelper();
		return mJwtTokenHelper;
	}

	// retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	// retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	// for retrieveing any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	// check if the token has expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		System.out.println(expiration.toString() + " " + new Date());
		return expiration.before(new Date());
	}

	// generate token for user
	public String generateToken(String username, Map<String, Object> info) {
		if(null == info)
			info = new HashMap<>();
		Map<String, Object> claims = new HashMap<>(info);
		return doGenerateToken(claims, username);
	}

	// while creating the token -
	// 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
	// 2. Sign the JWT using the HS512 algorithm and secret key.
	// 3. According to JWS Compact
	// Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	// compaction of the JWT to a URL-safe string
	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	// validate token
	public Boolean validateToken(String token) {
		return !isTokenExpired(token);
	}

	public Object getClaim(String token, String claim) {
		Claims claims = getAllClaimsFromToken(token);
		return claims.get(claim);
	}
}
