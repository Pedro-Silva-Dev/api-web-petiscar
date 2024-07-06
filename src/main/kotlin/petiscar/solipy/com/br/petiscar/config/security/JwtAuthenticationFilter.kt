package petiscar.solipy.com.br.petiscar.config.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import lombok.extern.log4j.Log4j2
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import petiscar.solipy.com.br.petiscar.models.dtos.UserRequestDto
import petiscar.solipy.com.br.petiscar.models.main.User
import petiscar.solipy.com.br.petiscar.repositories.main.UserRepository
import petiscar.solipy.com.br.petiscar.services.utils.HashidService

@Log4j2
@RequiredArgsConstructor
@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val hashidService: HashidService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filter: FilterChain
    ) {
        val authHeader = getAuthHeader(request)
        val jwtToken: String?
        val userKey: String?

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filter.doFilter(request, response)
            return
        }
        jwtToken = authHeader.substring(7)
        userKey = jwtService.getUserKey(jwtToken)
        request.setAttribute("userRequest", getUserRequest(jwtToken))

        if (userKey != null && SecurityContextHolder.getContext().authentication == null) {
            val userId = hashidService.toLongUserKey(userKey)
            val user = userRepository.findById(userId).orElse(null)
            if (user != null && jwtService.isTokenValid(jwtToken, user)) {
                val authToken = UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.authorities
                )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        }
        filter.doFilter(request, response)
    }

    private fun getAuthHeader(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")
    }

    private fun getUserRequest(jwtToken: String): UserRequestDto {
        val claims = jwtService.getAllClaims(jwtToken)
        return UserRequestDto(
            id = hashidService.toLongUserKey(claims["userKey"].toString()),
            companyId = claims["companyKey"]?.let { hashidService.toLongUserKey(it.toString()) } ?: 0,
            username = claims.subject,
            name = ""
        )
    }
}
