package com.example.demo.controller

import com.example.demo.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.example.demo.repository.UserRepository

@RestController
@RequestMapping("/api")
class UserController(@Autowired private val userRepository: UserRepository) {

    //get all users
    @GetMapping("/users")
    fun getAllUsers(): List<User> {
        return userRepository.findAll().toList()
    }

    //create user
    @PostMapping("/create")
    fun createUser(@RequestBody user: User): ResponseEntity<User> {
        val savedUser = userRepository.save(user)
        return ResponseEntity(savedUser, HttpStatus.CREATED)
    }

    //get user by id
    @GetMapping("/users/{id}")
    fun getUserById(@PathVariable("id") userId: Int): ResponseEntity<User> {
        val foundUser = userRepository.findById(userId).orElse(null)
        return if(foundUser != null) {
            ResponseEntity(foundUser, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    //update user
    @PutMapping("/update/{id}")
    fun updateUser(@PathVariable("id") userId: Int, @RequestBody user: User): ResponseEntity<User> {
        val existingUser = userRepository.findById(userId).orElse(null)
        if(existingUser == null) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            val updatedUser = existingUser.copy(name = user.name, email = user.email)
            userRepository.save(updatedUser)
            return ResponseEntity(updatedUser, HttpStatus.OK)
        }
    }

    //delete user
    @DeleteMapping("/delete/{id}")
    fun deleteUserById(@PathVariable("id") userId: Int): ResponseEntity<User> {
        if(!userRepository.existsById(userId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            userRepository.deleteById(userId)
            return ResponseEntity(HttpStatus.NO_CONTENT)
        }

    }
}


