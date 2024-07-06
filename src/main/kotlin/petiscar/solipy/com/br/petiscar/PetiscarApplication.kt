package petiscar.solipy.com.br.petiscar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class PetiscarApplication

fun main(args: Array<String>) {
	runApplication<PetiscarApplication>(*args)
}
