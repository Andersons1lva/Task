package com.anderson.task.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.anderson.task.R
import com.anderson.task.databinding.FragmentHomeBinding
import com.anderson.task.ui.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth

        configTabLayout()

        initClicks()
    }

    // Configuração da tabLayout
    private fun configTabLayout(){
        val adapter = ViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        adapter.addFragment(TodoFragment(), "A Fazer")
        adapter.addFragment(DoingFragment(), "Fazendo")
        adapter.addFragment(DoneFragment(), "Feitas")

        binding.viewPager.offscreenPageLimit = adapter.itemCount

        TabLayoutMediator(
            binding.tabs, binding.viewPager
        ){ tab, position ->
            tab.text = adapter.getTitle(
                position
            )
        }.attach()
    }
    //função de clicks geral
    private fun initClicks() {
        binding.ibLogout.setOnClickListener{logoutApp()}
    }

    private fun logoutApp(){
        //auth.singOut() desloga usuario do app
        auth.signOut()
        //após sair do app volta para tela de login
        findNavController().navigate(R.id.action_homeFragment_to_authentication)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}