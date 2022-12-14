package com.anderson.task.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.anderson.task.R
import com.anderson.task.databinding.FragmentRecoverAccountBinding
import com.anderson.task.helper.BaseFragment
import com.anderson.task.helper.FirebaseHelper
import com.anderson.task.helper.initToolBar
import com.anderson.task.helper.showBottomSheet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RecoverAccountFragment : BaseFragment() {

    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRecoverAccountBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolBar(binding.toolbar)

        // Initialize Firebase Auth
        auth = Firebase.auth

        initClicks()
    }

    private fun initClicks() {
        //botão de Login recebendo o metodo de validação
        binding.btnSend.setOnClickListener { validateData() }
    }

    //Função valida dados Confere os se os campos foram preenchidos
    private fun validateData() {

        val email = binding.edtEmail.text.toString().trim()

        // verificação se foi digitado algo no campo
        if (email.isNotEmpty()) {

            //oculta o teclado
            hideKeyboard()

            // após verificação exibir progressBar
            binding.progressBar.isVisible = true

            // após o progressBar chamar função registerUser
            recoverAccountUser(email)

        } else {
            showBottomSheet(
                message = R.string.text_email_empty_recover_account_fragment
            )
        }
    }

    //função responsavel por recuperar a conta
    private fun recoverAccountUser(email: String) {
        //recuperação da conta
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    showBottomSheet(
                        message = R.string.text_email_send_sucess_recover_account_fragment
                    )
                    //se não mostre a progressbar
                }else{
                    //retorna a mensagem de erro
                    showBottomSheet(
                      message =  FirebaseHelper.validError(task.exception?.message ?: "")
                    )
                }
                binding.progressBar.isVisible = false
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}