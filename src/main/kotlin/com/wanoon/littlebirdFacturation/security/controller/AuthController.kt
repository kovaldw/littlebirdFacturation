package com.wanoon.littlebirdFacturation.security.controller


import com.wanoon.littlebirdFacturation.security.repository.RoleRepository
import com.wanoon.littlebirdFacturation.security.repository.UserRepository
import com.wanoon.littlebirdFacturation.security.model.Role
import com.wanoon.littlebirdFacturation.security.model.RoleName
import com.wanoon.littlebirdFacturation.security.model.User
import com.wanoon.payload.responses.ApiResponse
import com.wanoon.littlebirdFacturation.security.authentication.JwtTokenProvider
import com.wanoon.littlebirdFacturation.security.payload.requests.LoginRequest
import com.wanoon.littlebirdFacturation.security.payload.requests.SignupRequest
import com.wanoon.littlebirdFacturation.security.payload.responses.JwtAuthenticationResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.util.*
import javax.validation.Valid


@RestController
@RequestMapping("/api/auth")
class AuthController
{

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var passwordEncoder: BCryptPasswordEncoder

    @Autowired
    lateinit var tokenProvider:JwtTokenProvider

    @PostMapping("/login")
    fun loginUser(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any>
    {
        var authentication: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.usernameOrEmail, loginRequest.password)
        )
//        return ResponseEntity.ok(loginRequest)

        SecurityContextHolder.getContext().authentication = authentication

        var jwt: String = tokenProvider.generateToken(authentication)

        return ResponseEntity.ok(JwtAuthenticationResponse(jwt))
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody signUpRequest: SignupRequest):ResponseEntity<Any>
    {
//        return ResponseEntity.ok(signUpRequest)
//        if (userRepository.existsByUsername(signUpRequest.username)) {
//            return ResponseEntity<Any>(ApiResponse(false, "Username is already taken!"),
//                    HttpStatus.BAD_REQUEST)
//        }

        if (userRepository.existsByEmail(signUpRequest.email)) {
            return ResponseEntity<Any>(ApiResponse(false, "Cette adresse email existe deja!"),
                    HttpStatus.BAD_REQUEST)
        }


        // Creation de l'utilisateur
        val user = User(null, signUpRequest.name, signUpRequest.username,
                signUpRequest.email, signUpRequest.password, signUpRequest.password)

        user.password = passwordEncoder.encode(user.password)

//        val userRole: Role = roleRepository.findByName(RoleName.ROLE_USER) ?: throw AppException("Role non défini")
        val userRole: Role = roleRepository.findByName(RoleName.ROLE_USER) ?: throw Exception("Role User non defini dans la base")

        user.roles = (Collections.singleton(userRole))

        val result: User = userRepository.save(user)

        val location: URI = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.username).toUri()

        return ResponseEntity.created(location).body<Any>(ApiResponse(true, "Utilisateur enregistre avec succes"))

    }


}