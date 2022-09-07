package com.anderson.task.helper

import com.anderson.task.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseHelper {
    //companion object e para fazer chamadas facilmente sem precisar estânciar a class FirebaseHelper
    companion object {
        //para recuperar a instancia do Bancon de dados(retorna sempre a referencia do banco de dados)
        fun getDataBase() = FirebaseDatabase.getInstance().reference

        //recupera a instancia de autenticação do usuário
        private fun getAuth() = FirebaseAuth.getInstance()

        //recupera o Id do usuário que está logado
        fun getIdUser() = getAuth().uid

        // verificar se usuario está logado no app
        fun isAutnticated() = getAuth().currentUser != null

        //pegar os erros que vem do firebase em ingles e jogar em português
        fun validError(error: String): Int {
            return when {
                //error quando não e um email cadastrado
                error.contains("There is no user record corresponding to this identifier") -> {
                    //pega a string definida para o error
                    R.string.account_not_registered_register_fragment
                }
                // error quando e uma email inválido(tipo sem @)
                error.contains("The email address is badly formatted") -> {
                    R.string.invalid_email_registrar_fragment
                }
                // error quando a senha for inválida
                error.contains("The password is invalid or the user does not have a password") -> {
                    R.string.invalid_password_register_fragment
                }
                // error quando tenta se cadastrar com email já cadastrado
                error.contains("The email address is already in use by another account") -> {
                    R.string.email_in_use_register_fragment
                }
                // error quando a senha e fraca e com menos de 6 caracteres
                error.contains("The given password is invalid. [ Password should be at least 6 characters ]") -> {
                    R.string.strong_password_register_fragment
                }
                else -> {
                    //se não for nenhum do error exibir uma string generica
                    R.string.error_generic
                }
            }
        }
    }
}