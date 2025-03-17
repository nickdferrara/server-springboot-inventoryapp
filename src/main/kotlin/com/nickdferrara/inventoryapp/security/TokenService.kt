package com.nickdferrara.inventoryapp.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
const val EXPIRY_TIME_IN_HOURS = 24L

@Service
class TokenService(private val jwtEncoder: JwtEncoder) {

    fun generateToken(authentication: Authentication): String {
        val now = Instant.now()
        val scope = authentication.authorities.joinToString(" ") { authority: GrantedAuthority -> authority.authority }

        val claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(EXPIRY_TIME_IN_HOURS, ChronoUnit.HOURS))
            .subject(authentication.name)
            .claim("scope", scope)
            .build()

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).tokenValue
    }
}