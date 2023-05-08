package hackathon.sprinter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SprinterApplication

fun main(args: Array<String>) {
	runApplication<SprinterApplication>(*args)
	println(System.getenv())
}
