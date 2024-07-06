package petiscar.solipy.com.br.petiscar.config.security

import org.springframework.security.authentication.AuthenticationProvider


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationProvider : AuthenticationProvider {

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication?): Authentication? {
        val name = authentication?.name
        val password = authentication?.credentials.toString()
        return if (name == null) {
            null
        } else {
            authenticateAgainstThirdPartyAndGetAuthentication(name, password, authentication.authorities)
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }

    private fun authenticateAgainstThirdPartyAndGetAuthentication(name: String, password: String, roles: Collection<GrantedAuthority>): UsernamePasswordAuthenticationToken {
        val grantedAuths: MutableList<GrantedAuthority> = roles.toMutableList()
        val principal: UserDetails = User(name, password, grantedAuths)
        return UsernamePasswordAuthenticationToken(principal, password, grantedAuths)
    }
}