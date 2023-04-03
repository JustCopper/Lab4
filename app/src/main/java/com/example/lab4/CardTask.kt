package com.example.lab4

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lab4.databinding.CurrentFragmentCardBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalTime
import java.util.*

class CardTask(var taskItem: TaskItem?) : BottomSheetDialogFragment()
{
    private lateinit var binding: CurrentFragmentCardBinding
    private lateinit var taskViewModel: TaskViewModel
    private var dueTime: LocalTime? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        taskViewModel = ViewModelProvider(activity).get(TaskViewModel::class.java)

        val mPickTimeBtn = binding.timePickerButton
        val bcomplete = binding.completeButton
        val bEdit = binding.editButton
        val editable = Editable.Factory.getInstance()
        bcomplete.text = if (taskItem?.completed == true) "Отменить выполнение" else "Выполнить"
        binding.name.text = editable.newEditable(taskItem!!.name)
        binding.desc.text = editable.newEditable(taskItem!!.desc)
        binding.timePickerButton.text = taskItem!!.dueDate
        mPickTimeBtn.setEnabled(false)
        binding.name.setEnabled(false)
        binding.desc.setEnabled(false)
        bcomplete.setOnClickListener{
            taskViewModel.setCompleted(taskItem!!)
            if (taskItem!!.completed == true){

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Успех!")
                .setMessage("Поздравляем. Задача выполнена")
                .setPositiveButton("ОК") {
                        dialog, id ->  dialog.cancel()
                }
            builder.create()
            builder.show()
            }

            dismiss()
        }
        bEdit.setOnClickListener{
            dismiss()
//                requireActivity().run{
//                    startActivity(Intent(this, NewTaskSheet::class.java))
//
//            }
            NewTaskSheet(taskItem).show((activity as FragmentActivity).supportFragmentManager, "newTaskTag")
        }

//        val c = Calendar.getInstance()
//        val year = c.get(Calendar.YEAR)
//        val month = c.get(Calendar.MONTH)
//        val day = c.get(Calendar.DAY_OF_MONTH)
//
//        mPickTimeBtn.setOnClickListener {
//
//            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//                // Display Selected date in TextView
//                val tempMonth = if (monthOfYear < 10) ("0" + (monthOfYear+1)) else (monthOfYear+1)
//                val tempday = if (dayOfMonth <10) "0"+dayOfMonth else dayOfMonth
//
//                mPickTimeBtn.text = ("" + tempday + "." + tempMonth + "." + year)
//                //textView. setText("" + dayOfMonth + " " + month + ", " + year)
//            }, year, month, day)
//            dpd.show()
//
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = CurrentFragmentCardBinding.inflate(inflater,container,false)
        return binding.root
    }
}