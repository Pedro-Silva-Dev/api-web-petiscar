package petiscar.solipy.com.br.petiscar.models.main

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import lombok.Data
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

@EntityListeners(AuditingEntityListener::class)
@Data
@Entity
@Table(name = "usuarios")
class User : UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "nome")
    var name: String? = null

    @Column(name = "email")
    var email: String? = null

    @Column(name = "senha")
    private var password: String = ""

    @Column(name = "telefone")
    var phone: String? = null

    @Column(name = "conta_nao_expirada")
    var accountNonExpired: Boolean? = null

    @Column(name = "conta_nao_bloqueada")
    var accountNonLocked: Boolean? = null

    @Column(name = "credenciais_nao_expiradas")
    var credentialsNonExpired: Boolean? = null

    @Column(name = "ativo")
    var enable: Boolean? = null

    @Column(name = "token")
    var token: String? = null

    @CreatedDate
    @Column(name = "dhc")
    var dhc: LocalDateTime? = null

    @LastModifiedDate
    @Column(name = "dhu")
    var dhu: LocalDateTime? = null

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuarios_regras",
        joinColumns = [JoinColumn(name = "usuario_id")],
        inverseJoinColumns = [JoinColumn(name = "regra_id")]
    )
    var roles: List<Role> = ArrayList()

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return roles.map { SimpleGrantedAuthority(it.role) }
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String? {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return accountNonExpired ?: false
    }

    override fun isAccountNonLocked(): Boolean {
        return accountNonLocked ?: false
    }

    override fun isCredentialsNonExpired(): Boolean {
        return credentialsNonExpired ?: false
    }

    override fun isEnabled(): Boolean {
        return enable ?: false
    }

    fun setPassword(password: String) {
        this.password = password;
    }

    @PrePersist
    fun createUserToken() {
        this.token = UUID.randomUUID().toString()
    }
}