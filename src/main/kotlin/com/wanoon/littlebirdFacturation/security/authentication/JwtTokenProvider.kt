package com.wanoon.littlebirdFacturation.security.authentication

import com.wanoon.littlebirdFacturation.security.extensions.JWT_EXPIRE_IN
import com.wanoon.littlebirdFacturation.security.extensions.JWT_SECRET
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter
import kotlin.math.log


//generation du token
//validation du token
//recuperation de l'id de l'utilisateur à partir du token

@Component
class JwtTokenProvider
{

    @Value(JWT_SECRET)
    lateinit var jwtSecret: String

    @Value(JWT_EXPIRE_IN.toString())
    lateinit var jwtExpirationInMs:String

    companion object
    {
        val logger: Logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)
    }


    fun generateToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserPrincipal
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationInMs.toInt())


        //We will sign our JWT with our ApiKey secret
        var signatureAlgorithm:SignatureAlgorithm = SignatureAlgorithm.HS512
        val apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecret)
        val signingKey: Key = SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.jcaName)

        return Jwts.builder()
                .setSubject(userPrincipal.id.toString())
                .setIssuedAt(Date())
                .setExpiration(expiryDate)
                .signWith(signatureAlgorithm, signingKey)
                .compact()
    }

    fun getUserIdFromJWT(token: String?): Long {

        //We will sign our JWT with our ApiKey secret
        var signatureAlgorithm:SignatureAlgorithm = SignatureAlgorithm.HS512
        val apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecret)
        val signingKey: Key = SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.jcaName)

        val claims = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token)
                .body
        return claims.subject.toLong()
    }

    fun validateToken(authToken: String?): Boolean {
        try {
//            logger.error(authToken)

            //We will sign our JWT with our ApiKey secret
            var signatureAlgorithm:SignatureAlgorithm = SignatureAlgorithm.HS512
            val apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecret)
            val signingKey: Key = SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.jcaName)

            Jwts.parser().setSigningKey(signingKey).parseClaimsJws(authToken)
//            logger.error("token valid")
            return true
        } catch (ex: SignatureException) {
            logger.error("Invalid JWT signature ${ex.message}" )
        } catch (ex: MalformedJwtException) {
            logger.error("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            logger.error("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            logger.error("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            logger.error("JWT claims string is empty.")
        }
        return false
    }

}