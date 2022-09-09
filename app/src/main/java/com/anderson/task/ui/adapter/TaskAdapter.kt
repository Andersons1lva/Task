package com.anderson.task.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.anderson.task.R
import com.anderson.task.databinding.ItemAdapterBinding
import com.anderson.task.model.Task

class TaskAdapter(
    private val context: Context,
    private val taskList: List<Task>,
    val taskSelected: (Task, Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.MyViewHolder>() {

    //identificação de qual botão está sendo clicado
    companion object {
        val SELECT_BACK: Int = 1
        val SELECT_REMOVE: Int = 2
        val SELECT_EDIT: Int = 3
        val SELECT_DETAILS: Int = 4
        val SELECT_NEXT: Int = 5
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = taskList[position]

        holder.binding.textDescription.text = task.description

        //Evento de clicks dos botões
        holder.binding.btnDelete.setOnClickListener { taskSelected(task,SELECT_REMOVE) }
        holder.binding.btnDelete.setOnClickListener { taskSelected(task, SELECT_DETAILS) }
        holder.binding.btnEdit.setOnClickListener { taskSelected(task, SELECT_EDIT) }

        //monitoramento do status
        when (task.status) {
            //tratamendo da seta dentro do fragment a fazer
            0 -> {
                holder.binding.ibBack.isVisible = false

                //mudanção da cor da seta
                holder.binding.ibNext.setColorFilter(
                    ContextCompat.getColor(context, R.color.color_doing)
                )

                //EVENTO DE CLICK Das setas
                holder.binding.ibNext.setOnClickListener { taskSelected(task, SELECT_NEXT) }
            }
            //tratamento para fragment fazendo
            1 -> {
                //mudanção da cor da seta
                holder.binding.ibBack.setColorFilter(
                    ContextCompat.getColor(context, R.color.color_todo)
                )

                //mudanção da cor da seta
                holder.binding.ibNext.setColorFilter(
                    ContextCompat.getColor(context, R.color.color_done)
                )
                //EVENTO DE CLICK DOS BOTÕES
                holder.binding.ibBack.setOnClickListener { taskSelected(task, SELECT_BACK) }
                holder.binding.ibNext.setOnClickListener { taskSelected(task, SELECT_NEXT) }


            }
            else -> {

                holder.binding.ibNext.isVisible = false
                //mudanção da cor da seta
                holder.binding.ibBack.setColorFilter(
                    ContextCompat.getColor(context, R.color.color_doing)
                )

                //EVENTO DE CLICK DOS BOTÕES
                holder.binding.ibBack.setOnClickListener { taskSelected(task, SELECT_BACK) }
            }


        }
    }

    override fun getItemCount() = taskList.size

    inner class MyViewHolder(val binding: ItemAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

}