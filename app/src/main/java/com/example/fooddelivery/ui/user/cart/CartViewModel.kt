package com.example.fooddelivery.ui.user.cart

import androidx.lifecycle.ViewModel
import com.example.fooddelivery.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel(){





}