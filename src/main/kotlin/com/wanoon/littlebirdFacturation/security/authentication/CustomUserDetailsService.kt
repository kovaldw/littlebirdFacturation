package com.wanoon.littlebirdFacturation.security.authentication

import com.wanoon.littlebirdFacturation.security.repository.UserRepository
import com.wanoon.littlebirdFacturation.security.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

//Cest cette classe qui contient les utilisateurs (permet de charger depuis la base de données un utilisateur
//grace a son mail ou son login

@Component
class CustomUserDetailsService() : UserDetailsService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Throws(UsernameNotFoundException::class)
    @Transactional
    override fun loadUserByUsername(usernameOrMail: String): UserDetails
    {

        //Utilisé par spring boot pour authentifier les utilisateurs
        var user: User? = userRepository.findByUsernameOrEmail(usernameOrMail, usernameOrMail)
                ?: throw UsernameNotFoundException("Utilisateur introuvable avec cette adresse mail ou login : $usernameOrMail")

        return UserPrincipal.create(user!!)
    }


    //Utilisé par JWT pour recuperer un utilisateur
    fun loadUserById(id:Long):UserDetails
    {
        var user:User? = userRepository.findById(id) ?: throw UsernameNotFoundException("Utilisateur introuvable avec cet id: $id")

        return UserPrincipal.create(user!!)

    }
}