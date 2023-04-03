package com.example.lab4

//import com.example.todolisttutorial.databinding.FragmentNewTaskSheetBinding

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lab4.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class NewTaskSheet(var taskItem: TaskItem?) : BottomSheetDialogFragment()
{
    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel: TaskViewModel
    private var dueTime: LocalTime? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        val coloredtitle = SpannableString("Название *")
        val coloredtimelabel = SpannableString("Срок выполнения *")
        coloredtitle.setSpan(ForegroundColorSpan(Color.RED), coloredtitle.length.toInt()-1, coloredtitle.length.toInt(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        coloredtimelabel.setSpan(ForegroundColorSpan(Color.RED), coloredtimelabel.length.toInt()-1, coloredtimelabel.length.toInt(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE)

        binding.labelTitle.text = coloredtitle
        binding.labeltimepicker.text = coloredtimelabel
        if (taskItem != null)
        {
            binding.deleteButton2.visibility = View.VISIBLE
            binding.taskTitle.text = "Изменить задачу"
            val editable = Editable.Factory.getInstance()
            binding.name.text = editable.newEditable(taskItem!!.name)
            binding.desc.text = editable.newEditable(taskItem!!.desc)
            binding.timePickerButton.text = taskItem!!.dueDate
            if(taskItem!!.dueTime != null){
                dueTime = taskItem!!.dueTime!!

                updateTimeButtonText()
            }

        }
        else
        {
            binding.deleteButton2.visibility = View.GONE
            binding.taskTitle.text = "Новая задача"
            println(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
            binding.timePickerButton.text = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

        }

        taskViewModel = ViewModelProvider(activity).get(TaskViewModel::class.java)
        binding.saveButton.setOnClickListener {
            saveAction()
        }
        binding.timePickerButton.setOnClickListener {
            openTimePicker()

        }
        binding.deleteButton2.setOnClickListener{
            taskViewModel.deleteTaskItem(taskItem!!)
            dismiss()
        }
        val mPickTimeBtn = binding.timePickerButton


        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        mPickTimeBtn.setOnClickListener {

            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                val tempMonth = if (monthOfYear < 10) ("0" + (monthOfYear+1)) else (monthOfYear+1)
                val tempday = if (dayOfMonth <10) "0"+dayOfMonth else dayOfMonth

                mPickTimeBtn.text = ("" + tempday + "." + tempMonth + "." + year)
                //textView. setText("" + dayOfMonth + " " + month + ", " + year)
            }, year, month, day)
            dpd.show()

        }
    }

    private fun openTimePicker() {
        if(dueTime == null)
            dueTime = LocalTime.now()
        val listener = TimePickerDialog.OnTimeSetListener{ _, selectedHour, selectedMinute ->
            dueTime = LocalTime.of(selectedHour, selectedMinute)
            updateTimeButtonText()
        }
        val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, true)
        dialog.setTitle("Task Due")
        dialog.show()

    }

//    private fun openDatePicker(){
//        var cal = Calendar.getInstance()
//
//        val dpd = DatePickerDialog(, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//
//            cal.set(Calendar.YEAR, year)
//            cal.set(Calendar.MONTH, monthOfYear)
//            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//
//            val myFormat = "dd.MM.yyyy" // mention the format you need
//            val sdf = SimpleDateFormat(myFormat, Locale.US)
//            // Display Selected date in textbox
//            binding.timePickerButton.text = cal.time.toString()
//
//        }, year, month, day)
//    }

    private fun updateTimeButtonText() {
        binding.timePickerButton.text = String.format("%02d:%02d",dueTime!!.hour,dueTime!!.minute)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewTaskSheetBinding.inflate(inflater,container,false)
        return binding.root
    }


    private fun saveAction()
    {

        val name = binding.name.text.toString()
        val desc = binding.desc.text.toString()
        val dueDate = if (binding.timePickerButton.text.toString() == "Выбрать время") null else binding.timePickerButton.text.toString()
//        println(name)
        if (name == " " || name == "" || dueDate == null){
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Ошибка")
                .setMessage("Введите обязательные поля")
                .setPositiveButton("ОК") {
                        dialog, id ->  dialog.cancel()
                }
            builder.create()
            builder.show()
            return
        }

        val overdue = if (dueDate == null) null else (LocalDate.parse(dueDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")) <= LocalDate.now())

        if(taskItem == null)
        {
            val newTask = TaskItem(name,desc,dueTime,dueDate,false, overdue)
            taskViewModel.addTaskItem(newTask)
        }
        else
        {
            //val overdue = (LocalDate.parse(dueDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")) <= LocalDate.now())
            taskViewModel.updateTaskItem(taskItem!!.id, name, desc, dueTime, dueDate,overdue)
            println(taskItem)
        }
        binding.name.setText("")
        binding.desc.setText("")
        binding.timePickerButton.text = ""
        dismiss()
    }

}








