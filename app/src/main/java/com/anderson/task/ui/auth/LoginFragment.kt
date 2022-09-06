package com.anderson.task.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.anderson.task.R
import com.anderson.task.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Configuração do viewbinding / Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth

        initClicks()
    }

    //Função para ouvir os enventos de clicks
    private fun initClicks(){
        //botão de Login recebendo o metodo de validação
        binding.btnLogin.setOnClickListener { validaData() }

        binding.btnRegister.setOnClickListener {
            //captura o envento de click, fazendo a navegação entre os fragment (Login com register)
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.btnRecover.setOnClickListener {
            //captura o envento de click, fazendo a navegação entre os fragment (Login com recover)
            findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
        }
    }

    //Função valida dados Confere os se os campos foram preenchidos
    private fun validaData() {
        val email = binding.edtEmailLogin.text.toString().trim()
        val password = binding.edtPasswordLogin.text.toString().trim()
        // verificação se foi digitado algo no campo
        if (email.isNotEmpty()) {
            if (password.isNotEmpty()) {

                // após verificação exibir progressBar
                binding.progressBar.isVisible = true

                // após o progressBar chamar função registerUser
                loginUser(email, password)

            } else {
                Toast.makeText(requireContext(), "Informe sua senha", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Informe seu email", Toast.LENGTH_SHORT).show()
        }
    }

    //função responsavel de salvar os dados no Firebase
    private fun loginUser(email: String, password: String) {
        //Criação de email e senha
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    //se criação ok direcionar para homeFragment
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    //se não mostre a progressbar
                    binding.progressBar.isVisible = false
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
 }