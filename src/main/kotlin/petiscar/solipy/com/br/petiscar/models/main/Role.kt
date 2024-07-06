package petiscar.solipy.com.br.petiscar.models.main

import jakarta.persistence.*
import lombok.*

@Data
@Entity
@Table(name = "regras")
data class Role(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "nome")
    val name: String,

    @Column(name = "regra")
    val role: String,

    @Column(name = "descricao")
    val description: String
)