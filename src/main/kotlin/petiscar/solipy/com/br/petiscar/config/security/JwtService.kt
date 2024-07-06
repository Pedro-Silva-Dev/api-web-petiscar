package petiscar.solipy.com.br.petiscar.config.security

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import lombok.RequiredArgsConstructor
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service
import petiscar.solipy.com.br.petiscar.models.main.Role
import petiscar.solipy.com.br.petiscar.models.main.User
import petiscar.solipy.com.br.petiscar.services.utils.HashidService
import java.util.*
import java.security.Key;
import java.util.stream.Collectors
import kotlin.reflect.KFunction1

@RequiredArgsConstructor
@Service
class JwtService(
    val hashidService: HashidService
) {

    private val logger = LogManager.getLogger()

    val SECRET_KEY = "6B58703273357638792F423F4528482B4B6250655368566D597133743677397A24432646294A404E635166546A576E5A7234753778214125442A472D4B615064";

    fun getUserToken(jwtToken: String): String {
        return getClaim(jwtToken, Claims::getSubject)
    }

    fun getUserKey(jwtToken: String): String {
        val claims = getAllClaims(jwtToken);
        return claims.get("userKey").toString();
    }

    fun <T> getClaim(jwtToken: String, claimsResolver: (Claims) -> T): T {
        val claims = getAllClaims(jwtToken)
        return claimsResolver(claims)
    }

    fun generateToken(user: User): String {
        return generateToken(HashMap(), user);
    }

    fun generateToken(extraClaims: Map<String, Object>, user: User): String {
        val roles = user.roles.map{it.role}.joinToString(",")
        val userId = user.id ?: 0;
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(user.token)
            .claim("userKey", hashidService.get(32).encode(userId))
            .claim("name", user.name)
            .claim("roles", roles)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 10000000 * 60 * 24))
            .signWith(getSigngInKey(), SignatureAlgorithm.HS512)
            .compact();
    }

    fun isTokenValid(jwtToken: String, user: User): Boolean {
        val userId = hashidService.toLongUserKey(getUserKey(jwtToken))
        val userToken = getUserToken(jwtToken)
        return userId == user.id && userToken == user.token && !isTokenExpired(jwtToken)
    }


    fun getAllClaims(jwtToken: String): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSigngInKey())
            .build()
            .parseClaimsJws(jwtToken)
            .getBody();
    }

    fun isTokenExpired(jwtToken: String): Boolean {
        return getExpiration(jwtToken).before(Date());
    }

    private fun getExpiration(jwtToken: String): Date {
        return getClaim(jwtToken, Claims::getExpiration)
    }


    private fun getSigngInKey(): Key {
        val keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}