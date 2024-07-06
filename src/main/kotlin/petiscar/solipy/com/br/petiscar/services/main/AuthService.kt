package petiscar.solipy.com.br.petiscar.services.main

import lombok.RequiredArgsConstructor
import lombok.extern.log4j.Log4j2
import org.apache.logging.log4j.LogManager
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import petiscar.solipy.com.br.petiscar.config.security.JwtService
import petiscar.solipy.com.br.petiscar.models.main.Role
import petiscar.solipy.com.br.petiscar.models.main.User
import petiscar.solipy.com.br.petiscar.models.requests.main.AuthRequest
import petiscar.solipy.com.br.petiscar.models.requests.main.RegisterNewUserRequest
import petiscar.solipy.com.br.petiscar.models.responses.main.AuthResponse
import petiscar.solipy.com.br.petiscar.repositories.main.RoleRepository
import petiscar.solipy.com.br.petiscar.repositories.main.UserRepository
import kotlin.jvm.optionals.getOrNull

@RequiredArgsConstructor
@Log4j2
@Service
class AuthService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationProvider: AuthenticationProvider,
) {

    private val logger = LogManager.getLogger()

    fun signin(authRequest: AuthRequest): AuthResponse {
        val user = userRepository.findByEmail(authRequest.username).getOrNull() ?: throw IllegalArgumentException("User not found")
        val jwtToken = jwtService.generateToken(user)
        authenticationProvider.authenticate(UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password, user.authorities))
        return AuthResponse(token = jwtToken)
    }

    fun register(registerNewUserRequest: RegisterNewUserRequest): AuthResponse {
        val user = User()
        user.name = registerNewUserRequest.name
        user.email = registerNewUserRequest.email
        user.password = passwordEncoder.encode(registerNewUserRequest.password)
        user.phone = registerNewUserRequest.phone
        user.roles = getRoles()
        user.enable = true
        user.accountNonExpired = true
        user.accountNonLocked = true
        user.credentialsNonExpired = true

        val userSave = userRepository.save(user)
        val jwtToken = jwtService.generateToken(userSave)
        return AuthResponse(token = jwtToken)
    }

    private fun getRoles(): List<Role> {
        return roleRepository.findByRoleUser().getOrNull()?.let { listOf(it) } ?: emptyList()
    }
}
