package com.example.lab4

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class TaskViewModel: ViewModel()
{
    var taskItems = MutableLiveData<MutableList<TaskItem>>()

    init {
        taskItems.value = mutableListOf()
    }

    fun addTaskItem(newTask: TaskItem)
    {
        val list = taskItems.value
        list!!.add(newTask)
        //list.sortBy { it.dueDate == null } // сделать сортировку по 2 признакам!
        list.sortWith(compareBy<TaskItem> {it.completed  }.thenBy { LocalDate.parse( it.dueDate,
            DateTimeFormatter.ofPattern("dd.MM.yyyy")) })
        taskItems.postValue(list)

    }
    fun deleteTaskItem(newTask: TaskItem)
    {
        val list = taskItems.value
        val task = list!!.find { it.id == newTask.id }!!
        list!!.remove(task)
        list.sortWith(compareBy<TaskItem> {it.completed  }.thenBy { LocalDate.parse( it.dueDate,
            DateTimeFormatter.ofPattern("dd.MM.yyyy")) })
        taskItems.postValue(list)


    }

    fun updateTaskItem(id: UUID, name: String, desc: String, dueTime: LocalTime?,dueDate: String?,overdue: Boolean?)
    {
        val list = taskItems.value
        val task = list!!.find { it.id == id }!!

        task.name = name
        task.desc = desc
        task.dueTime = dueTime
        task.dueDate = dueDate
        task.overdue = overdue
        list.sortWith(compareBy<TaskItem> {it.completed  }.thenBy { LocalDate.parse( it.dueDate,
            DateTimeFormatter.ofPattern("dd.MM.yyyy")) })
        taskItems.postValue(list)

    }

    fun setCompleted(taskItem: TaskItem)
    {
        val list = taskItems.value
        val task = list!!.find { it.id == taskItem.id }!!
        task.completed = if (task.completed == true)false else true
        list.sortWith(compareBy<TaskItem> {it.completed  }.thenBy { LocalDate.parse( it.dueDate,
            DateTimeFormatter.ofPattern("dd.MM.yyyy")) })
        taskItems.postValue(list)
    }
}