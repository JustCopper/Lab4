package com.example.lab4

import android.content.Context
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.example.lab4.TaskItem
import com.example.lab4.TaskItemClickListener
import com.example.lab4.databinding.TaskItemCellBinding
import java.time.LocalDate
import java.time.LocalDateTime
//import com.example.todolisttutorial.databinding.TaskItemCellBinding
import java.time.format.DateTimeFormatter

class TaskItemViewHolder(
    private val context: Context,
    private val binding: TaskItemCellBinding,
    private val clickListener: TaskItemClickListener
): RecyclerView.ViewHolder(binding.root)
{
    private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

    fun bindTaskItem(taskItem: TaskItem)
    {


        binding.name.text = if (taskItem.name.length > 15) taskItem.name.substring(0,14) + "..." else taskItem.name

        if (taskItem.completed == true){
            binding.name.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.desc.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.duedate.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

        }
        if (taskItem.overdue == true && taskItem.completed == false) {
            binding.duedate.setTextColor(-65536)
        }
//        else if (taskItem.overdue == null) {
//            binding.duedate.text = "1"
//        }
        else {
            binding.duedate.setTextColor(-16777216)
        }

//        if (LocalDate.parse(taskItem.dueDate) >= LocalDate.now()){
//            println("prosrok")
//        }

//        binding.completeButton.setImageResource(taskItem.imageResource())
//        binding.completeButton.setColorFilter(taskItem.imageColor(context))
//
//        binding.completeButton.setOnClickListener{
//            clickListener.completeTaskItem(taskItem)
//        }
        binding.taskCellContainer.setOnClickListener{
            clickListener.showTaskItem(taskItem)
        }

//        if(taskItem.dueTime != null)
//            binding.desc.text = "timeFormat.format(taskItem.dueTime)"
//        else
//            binding.desc.text = ""
        binding.desc.text = if (taskItem.desc.length > 70) taskItem.desc.substring(0,70) + "..." else taskItem.desc
//        binding.desc.text = taskItem.desc
        binding.duedate.text = if (taskItem.dueDate == null) "Не задано" else taskItem.dueDate
//        println(taskItem.dueDate)
    }
}