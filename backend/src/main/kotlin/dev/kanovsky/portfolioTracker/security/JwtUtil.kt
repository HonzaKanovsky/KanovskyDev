package dev.kanovsky.portfolioTracker.security

import dev.kanovsky.portfolioTracker.enums.TokenValidityCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.spec.SecretKeySpec

private val logger = LoggerFactory.getLogger(JwtUtil::class.java)

/**
 * Utility class for handling JWT token generation and validation.
 */
@Component
class JwtUtil {

    private val secretKey: String = System.getenv("JWT_SECRET")
        ?: throw IllegalStateException("JWT_SECRET environment variable is not set")

    private val key = SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS256.jcaName)

    /**
     * Generates an access token with a short expiry time (15 minutes).
     * @param userName The username for whom the token is generated.
     * @return The generated JWT access token.
     */
    fun generateAccessToken(userName: String): String {
        val token = Jwts.builder()
            .setSubject(userName)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * (TokenValidityCode.ONE_HOUR.seconds / 4))) // 15-minute expiry
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        logger.info("Generated access token for user: $userName")
        return token
    }

    /**
     * Generates a refresh token with a longer expiry time (1 day).
     * @param userName The username for whom the token is generated.
     * @return The generated JWT refresh token.
     */
    fun generateRefreshToken(userName: String): String {
        return Jwts.builder()
            .setSubject(userName)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * TokenValidityCode.ONE_DAY.seconds)) // 1-day expiry
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    /**
     * Extracts the username from a JWT token.
     * @param token The JWT token.
     * @return The username contained in the token, or null if extraction fails.
     */
    fun extractUsername(token: String): String {
        return extractClaims(token).subject
    }

    /**
     * Validates a JWT token against the provided username.
     * @param token The JWT token to validate.
     * @param userName The expected username.
     * @return `true` if the token is valid and belongs to the user, `false` otherwise.
     */
    fun isTokenValid(token: String, userName: String): Boolean {
        return try {
            val extractedUser = extractUsername(token)
            val isValid = extractedUser == userName && !isTokenExpired(token)

            if (!isValid) logger.warn("Invalid or expired JWT for user: $userName")

            isValid
        } catch (e: Exception) {
            logger.error("Error validating token: ${e.message}")
            false
        }
    }

    /**
     * Extracts claims (payload data) from a JWT token.
     * @param token The JWT token.
     * @return The claims contained in the token.
     * @throws Exception if the token is invalid.
     */
    private fun extractClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }

    /**
     * Checks if a JWT token is expired.
     * @param token The JWT token.
     * @return `true` if the token is expired, `false` otherwise.
     */
    private fun isTokenExpired(token: String): Boolean {
        return try {
            val expiration = extractClaims(token).expiration
            expiration.before(Date())
        } catch (e: Exception) {
            true
        }
    }
}
