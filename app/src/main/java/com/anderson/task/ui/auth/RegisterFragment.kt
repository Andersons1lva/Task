package com.anderson.task.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.anderson.task.R
import com.anderson.task.databinding.FragmentRegisterBinding
import com.anderson.task.helper.BaseFragment
import com.anderson.task.helper.FirebaseHelper
import com.anderson.task.helper.initToolBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegisterFragment : BaseFragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment / implementação do viewbinding
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolBar(binding.toolbar)

        // Initialize Firebase Auth
        auth = Firebase.auth
        //inicialização do metodo
        initClicks()
    }

    //para pegar quando clicar no criar conta
    private fun initClicks(){
        binding.btnRegister.setOnClickListener { validaData() }
    }

    //Função valida dados
    private fun validaData() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        // verificação se foi digitado algo no campo
        if (email.isNotEmpty()) {
            if (password.isNotEmpty()) {

                //oculta o teclado
                hideKeyboard()

                // após verificação exibir progressBar
                binding.progressBar.isVisible = true

                // após o progressBar chamar função registerUser
                registerUser(email, password)

            } else {
                Toast.makeText(requireContext(), "Informe sua senha", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Informe seu email", Toast.LENGTH_SHORT).show()
        }
    }

    //função responsavel de salvar os dados no Firebase
    private fun registerUser(email: String, password: String) {
        //Criação de email e senha
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    //se criação ok direcionar para homeFragment
                    findNavController().navigate(R.id.action_global_homeFragment)
                } else {
                    //retorna a mensagem de erro
                    Toast.makeText(
                        requireContext(),
                        FirebaseHelper.validError(task.exception?.message ?: ""),
                        Toast.LENGTH_SHORT
                    ).show()
                    //se não mostre a progressbar
                    binding.progressBar.isVisible = false
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}