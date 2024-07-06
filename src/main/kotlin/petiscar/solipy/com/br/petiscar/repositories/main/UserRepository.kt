package petiscar.solipy.com.br.petiscar.repositories.main

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository
import petiscar.solipy.com.br.petiscar.models.main.User
import java.util.*


@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(username: String): Optional<User>

    @Query(value = "SELECT u.* " +
                "FROM usuarios u " +
                "JOIN cargos c ON u.cargo_id = c.id " +
                "WHERE u.id = ?1 AND c.empresa_id = ?2", nativeQuery = true)
    fun findByIdAndCompanyId(id: Long, companyId: Long): Optional<User>
}
