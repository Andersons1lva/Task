package com.anderson.task.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.anderson.task.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment / implementação do viewbinding
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    //Função valida dados
    private fun validaData(){
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        // verificação se foi digitado algo no campo
        if (email.isNotEmpty()){
            if (password.isNotEmpty()){

                // após verificação exibir progressBar
                binding.progressBar.isVisible = true

                // após o progressBar chamar função registerUser
                registerUser(email,password)

            }else{
                Toast.makeText(requireContext(),"Informe sua senha",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(requireContext(),"Informe seu email",Toast.LENGTH_SHORT).show()
        }
    }

    //função responsavel de salvar os dados no Firebase
    private fun registerUser(email: String, password: String){

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}