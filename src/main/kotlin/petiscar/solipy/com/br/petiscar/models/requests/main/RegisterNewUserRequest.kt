package petiscar.solipy.com.br.petiscar.models.requests.main

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

data class RegisterNewUserRequest(
    @field:NotNull @field:NotEmpty val name: String,
    @field:NotNull @field:NotEmpty @field:Email val email: String,
    @field:NotNull @field:NotEmpty @field:Length(min = 8) val password: String,
    @field:NotNull @field:NotEmpty val phone: String
)
