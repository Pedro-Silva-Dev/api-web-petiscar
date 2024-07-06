package petiscar.solipy.com.br.petiscar.models.requests.main

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

data class AuthRequest(
    @field:NotNull @field:NotEmpty @field:Email val username: String,
    @field:NotNull @field:NotEmpty val password: String
)