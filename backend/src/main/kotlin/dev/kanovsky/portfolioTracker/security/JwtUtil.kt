package dev.kanovsky.portfolioTracker.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Component
class JwtUtil {
    private val secretKey = System.getenv("JWT_SECRET")

    private val key = SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS256.jcaName)

    fun generateAccessToken(userName: String): String {
        return Jwts.builder()
            .setSubject(userName)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 minute expiry
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun generateRefreshToken(userName: String): String {
        return Jwts.builder()
            .setSubject(userName) // Store user ID
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) //  1 days expiry
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }


    fun extractUsername(token: String): String? {
        return extractClaims(token).subject
    }

    fun isTokenValid(token: String, userName: String): Boolean {
        return extractUsername(token) == userName && !isTokenExpired(token)
    }

    private fun extractClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun isTokenExpired(token: String): Boolean {
        return try {
            val expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
                .expiration
            expiration.before(Date())
        } catch (e: Exception) {
            true
        }
    }
}
