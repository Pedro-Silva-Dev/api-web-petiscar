package petiscar.solipy.com.br.petiscar.repositories.main

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository
import petiscar.solipy.com.br.petiscar.models.main.Role
import java.util.*

@Repository
interface RoleRepository : JpaRepository<Role, Long> {

    @Query(value = "SELECT * FROM regras WHERE upper(regra) = 'ROLE_USER' LIMIT 1", nativeQuery = true)
    fun findByRoleUser(): Optional<Role>
}