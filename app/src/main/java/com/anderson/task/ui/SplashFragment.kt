package com.anderson.task.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.anderson.task.R
import com.anderson.task.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Configuração do viewbinding / Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Implementação do tempo da Splash
        Handler(Looper.getMainLooper()).postDelayed(this::checkAuth,3000)
    }

    //Método que check se usuário está logado
    private fun checkAuth(){
        // Initialize Firebase Auth
        auth = Firebase.auth
        //verificar se usuário está logado
        if (auth.currentUser == null) {
            //se auth.currentUser for nulo (action_splashFragment_to_authentication) então levara para tela de login
            findNavController().navigate(R.id.action_splashFragment_to_authentication)
        }else{
            // se auth.currentUser for diferente de nulo (action_splashFragment_to_homeFragment) então levar para tela Home
            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}