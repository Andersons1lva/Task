package com.anderson.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anderson.task.R
import com.anderson.task.databinding.FragmentDoneBinding
import com.anderson.task.databinding.FragmentTodoBinding
import com.anderson.task.helper.FirebaseHelper
import com.anderson.task.model.Task
import com.anderson.task.ui.adapter.TaskAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class DoneFragment : Fragment() {

    private var _binding: FragmentDoneBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter

    private val taskList = mutableListOf<Task>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDoneBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       getTasks()
    }


    //recuperação das tarefas do firebase
    private fun getTasks() {
        FirebaseHelper
            .getDataBase()
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        taskList.clear()
                        // se houver tarefas no firebase percorre o banco de de dados
                        for (snap in snapshot.children){
                            val task = snap.getValue(Task::class.java) as Task
                            if (task.status == 2) taskList.add(task)
                        }

                        taskList.reverse()
                        initAdapter()
                    }

                    tasksEmpty()
                    binding.progressBar.isVisible = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }

            })
    }

    // exibe mensagem quando não houver nenhuma tarefa cadastrada na tela
    private fun tasksEmpty(){
        binding.textInfo.text = if (taskList.isEmpty()){
            getString(R.string.text_task_list_empty_done_fragment)
        }else{
            ""
        }
    }

    private fun initAdapter(){
        binding.rvTask.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTask.setHasFixedSize(true)
        taskAdapter = TaskAdapter(requireContext(), taskList){ task, select ->
            optionSelect(task,select)
        }
        binding.rvTask.adapter = taskAdapter
    }

    // captura o click no botão Remove
    private fun optionSelect(task: Task, select: Int){
        when(select){
            TaskAdapter.SELECT_REMOVE -> {
                deleteTask(task)
            }
            TaskAdapter.SELECT_EDIT -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToFormTaskFragment(task)
                findNavController().navigate(action)
            }
            TaskAdapter.SELECT_BACK -> {
                task.status = 1
                updateTask(task)
            }
        }
    }

    // Tratamento do status pelas setas
    private fun updateTask(task: Task) {
        FirebaseHelper
            .getDataBase()
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .child(task.id)
            .setValue(task)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Tarefa atualizada com sucesso.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), "Erro ao salvar tarefa.", Toast.LENGTH_SHORT)
                        .show()
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Erro ao salvar tarefa.", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    //deleta tarefa do bonco de dado
    private fun deleteTask(task: Task){
        FirebaseHelper
            .getDataBase()
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .child(task.id)
            .removeValue()

        taskList.remove(task)
        taskAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}