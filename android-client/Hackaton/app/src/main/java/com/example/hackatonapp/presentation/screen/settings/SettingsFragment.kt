package com.example.hackatonapp.presentation.screen.settings

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hackatonapp.R
import com.example.hackatonapp.databinding.FragmentSettingsBinding
import com.example.hackatonapp.presentation.extensions.viewBinding
import com.example.hackatonapp.presentation.screen.sign_in.SignInViewModel
import java.util.*

class SettingsFragment :
    Fragment(R.layout.fragment_settings),
    TimePickerDialog.OnTimeSetListener {

    private val binding by viewBinding(FragmentSettingsBinding::bind)

    private val viewModel: SignInViewModel by viewModels()

    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setListeners()
        subscribeToViewModel()
    }

    private fun initViews() {}

    private fun setListeners() {
        binding.switch1.setOnClickListener {
            if (binding.switch1.isChecked) {
                val calendar: Calendar = Calendar.getInstance()
                hour = calendar.get(Calendar.HOUR)
                minute = calendar.get(Calendar.MINUTE)
                val timePickerDialog =
                    TimePickerDialog(view?.context!!, this, hour, minute, DateFormat.is24HourFormat(view?.context))
                timePickerDialog.show()
            } else {
                binding.tvWhenWeSendNotifications.visibility = View.GONE
            }
        }
        binding.buttonExit.setOnClickListener {
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            sharedPref?.edit()?.remove("token")?.apply()
            findNavController().navigate(R.id.initFragment)
        }
    }

    private fun subscribeToViewModel() {}

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        var timeLine: String
        timeLine = if (myMinute < 10){
            "$hourOfDay:0$minute"
        } else {
            "$hourOfDay:$minute"
        }
        binding.tvWhenWeSendNotifications.visibility = View.VISIBLE
        binding.tvWhenWeSendNotifications.text = resources.getString(R.string.when_we_send_notifications, timeLine);
    }
}
