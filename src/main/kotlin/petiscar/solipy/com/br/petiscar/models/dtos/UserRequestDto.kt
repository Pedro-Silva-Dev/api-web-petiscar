package petiscar.solipy.com.br.petiscar.models.dtos

import lombok.Data

@Data
data class UserRequestDto(
    val id: Long,
    val name: String,
    val username: String,
    val companyId: Long
)


