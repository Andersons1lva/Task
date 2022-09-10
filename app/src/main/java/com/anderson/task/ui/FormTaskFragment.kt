package com.anderson.task.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.anderson.task.R
import com.anderson.task.databinding.FragmentFormTaskBinding
import com.anderson.task.helper.FirebaseHelper
import com.anderson.task.model.Task


class FormTaskFragment : Fragment() {

    private val args: FormTaskFragmentArgs by navArgs()

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: Task
    private var newTask: Boolean = true
    private var statusTask: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        getArgs()
    }
    // tratamento do botão editar
    private fun getArgs(){
        args.task.let {
            if (it != null){
                task = it

                configTask()
            }
        }
    }

    //carregamento dos dados para editar
    private fun configTask(){
        newTask = false
        statusTask = task.status
        binding.textToolbar.text = "Editando Tarefa"

        //preenche o campo da descrição
        binding.edtDescription.setText(task.description)
        setStatus()
    }
    //faz a troca do status
    private fun setStatus(){
        binding.radioGroup.check(
            when(task.status){
                0 ->{
                    R.id.rbTodo
                }
                1 ->{
                    R.id.rbDoing
                }
                else ->{
                    R.id.rbDone
                }
            }
        )
    }

    //Caputurar o click do botão Salvar
    private fun initListeners() {
        binding.btnSave.setOnClickListener { validateData() }

        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            statusTask = when (id) {
                R.id.rbTodo -> 0
                R.id.rbDoing -> 1
                else -> 2
            }
        }
    }

    //valida o campo da descrição
    private fun validateData() {
        val description = binding.edtDescription.text.toString().trim()
        if (description.isNotEmpty()) {
            // exibir a progressbar
            binding.progressBar.isVisible = true

            if (newTask) task = Task()
            task.description = description
            task.status = statusTask

            saveTask()

        } else {
            Toast.makeText(
                requireContext(),
                "Informe uma descrição para a tarefa.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Salva a Descrição no Firebase
    private fun saveTask() {
        FirebaseHelper
            .getDataBase()
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .child(task.id)
            .setValue(task)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (newTask) {//Nova tarefa
                        findNavController().popBackStack()
                        Toast.makeText(
                            requireContext(),
                            "Tarefa salva com sucesso.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {// editando tarefa
                        binding.progressBar.isVisible = false
                        Toast.makeText(
                            requireContext(),
                            "Tarefa atualizada com sucesso.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Erro ao salvar tarefa.", Toast.LENGTH_SHORT)
                        .show()
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Erro ao salvar tarefa.", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}