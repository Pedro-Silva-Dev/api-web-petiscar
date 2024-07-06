package petiscar.solipy.com.br.petiscar.controllers.main

import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import petiscar.solipy.com.br.petiscar.models.requests.main.AuthRequest
import petiscar.solipy.com.br.petiscar.models.requests.main.RegisterNewUserRequest
import petiscar.solipy.com.br.petiscar.models.responses.main.AuthResponse
import petiscar.solipy.com.br.petiscar.services.main.AuthService

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
class AuthRestController(
    private val authService: AuthService
) {

    private val logger = LogManager.getLogger()

    @PostMapping("/register.json")
    fun register(@RequestBody @Valid registerNewUserRequest: RegisterNewUserRequest): ResponseEntity<AuthResponse> {
        val auth = authService.register(registerNewUserRequest)
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(auth)
    }

    @PostMapping("/signin.json")
    fun signin(@RequestBody @Valid authRequest: AuthRequest): ResponseEntity<AuthResponse> {
        val auth = authService.signin(authRequest)
        return ResponseEntity.status(HttpStatus.OK.value()).body(auth)
    }
}
