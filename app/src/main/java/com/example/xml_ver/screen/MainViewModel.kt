package com.example.xml_ver.screen

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xml_ver.R
import com.example.xml_ver.data.system.ColorOption
import com.example.xml_ver.data.system.Event
import com.example.xml_ver.util.SharedPreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val context: Context
) : ViewModel() {
    var beforeStack = MutableStateFlow("mainScreen")

    private val _mainColor = MutableStateFlow(ColorOption.BLUE)
    val mainColor: StateFlow<ColorOption> = _mainColor

    private val _colorState = MutableStateFlow(R.color.mainColor)
    val colorState: StateFlow<Int> = _colorState

    val role = SharedPreferenceUtil(context).getString("role", "USER")
    val nickname = SharedPreferenceUtil(context).getString("nickname", "")

    private val _notificationState =
        MutableStateFlow(SharedPreferenceUtil(context).getBoolean("notification", false))
    val notificationState: StateFlow<Boolean> = _notificationState

    fun changeNotificationAccess(enabled: Boolean) {
        viewModelScope.launch {
            _notificationState.value = enabled
            SharedPreferenceUtil(context).setBoolean("notification", enabled)
        }
    }

    init {
        val mainColor = SharedPreferenceUtil(context).getInt("themeColor", R.color.mainColor)
        _colorState.value = mainColor
    }

    fun updateColor(colorOption: ColorOption) {
        SharedPreferenceUtil(context).setInt("themeColor", colorOption.colorResId)
        viewModelScope.launch {
            _mainColor.emit(colorOption)
            _colorState.emit(colorOption.colorResId)
        }
    }

    fun getVersion(): String? {
        var versionName = ""
        val pm = context.packageManager.getPackageInfo(context.packageName, 0)
        versionName = pm.versionName

        return versionName
    }

    private val _navigateToAnotherActivity = MutableLiveData<Event<Intent>>()
    val navigateToAnotherActivity: LiveData<Event<Intent>> = _navigateToAnotherActivity
}