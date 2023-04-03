package com.example.lab4

import com.example.lab4.TaskItem

interface TaskItemClickListener
{
    fun editTaskItem(taskItem: TaskItem)
    fun showTaskItem(taskItem: TaskItem)
    fun deleteTaskItem(taskItem: TaskItem)
    fun completeTaskItem(taskItem: TaskItem)
}