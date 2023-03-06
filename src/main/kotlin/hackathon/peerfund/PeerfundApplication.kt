package hackathon.peerfund

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PeerfundApplication

fun main(args: Array<String>) {
	runApplication<PeerfundApplication>(*args)
}
