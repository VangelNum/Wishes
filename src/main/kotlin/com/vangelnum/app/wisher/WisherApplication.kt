package com.vangelnum.app.wisher

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class WisherApplication

fun main(args: Array<String>) {
	runApplication<WisherApplication>(*args)
}

@RestController
class DemoController(private val greetingRepository: GreetingRepository) {

	@GetMapping("/hello")
	fun hello(): String {
		return "Hello World!"
	}

	@GetMapping("/greetings")
	fun getAllGreetings(): List<Greeting> {
		return greetingRepository.findAll()
	}

	@PostMapping("/greetings")
	fun createGreeting(@RequestBody greeting: Greeting): ResponseEntity<Greeting> {
		val createdGreeting = greetingRepository.save(greeting)
		return ResponseEntity(createdGreeting, HttpStatus.CREATED)
	}
}

@Repository
interface GreetingRepository : JpaRepository<Greeting, Long>

@Entity
@Table(name = "greetings")
data class Greeting (
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	val message: String
)