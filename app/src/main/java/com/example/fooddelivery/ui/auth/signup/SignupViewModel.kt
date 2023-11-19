package com.example.fooddelivery.ui.auth.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelivery.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signedUp = MutableStateFlow("")
    val signedUp = _signedUp

    fun signUp(email: String, password: String) = viewModelScope.launch {
        try {
            authRepository.signup(email, password).let {
                _signedUp.value = it
                Log.d("ResultExcVm", it)
            }
        } catch (e: Exception) {
            _signedUp.value = e.message.toString()
            Log.d("ResultExcVM", e.message.toString())
        }
    }

}