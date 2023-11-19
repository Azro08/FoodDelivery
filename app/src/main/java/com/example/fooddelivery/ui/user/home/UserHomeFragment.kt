package com.example.fooddelivery.ui.user.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddelivery.data.model.Food
import com.example.fooddelivery.data.model.FoodCategory
import com.example.fooddelivery.databinding.FragmentUserHomeBinding
import com.example.fooddelivery.ui.user.home.adapter.CategoryRvAdapter
import com.example.fooddelivery.ui.user.home.adapter.FoodRvAdapter
import com.ivkorshak.el_diaries.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserHomeFragment : Fragment() {
    private var _binding: FragmentUserHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserHomeViewModel by viewModels()
    private var categoryRvAdapter: CategoryRvAdapter? = null
    private var foodRvAdapter: FoodRvAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getFoodCategories()
        getFoodList("all")
    }

    private fun getFoodCategories() {
        lifecycleScope.launch {
            viewModel.categoryList.collect { state ->
                when (state){
                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> handleError(state.message.toString())
                    is ScreenState.Success ->{
                        if (state.data.isNullOrEmpty()) handleError(state.message.toString())
                        else displayCategories(state.data)
                    }
                }
            }
        }
    }

    private fun displayCategories(foodCategories: List<FoodCategory>) {
        Log.d("FoodList", "food ${foodCategories.size}")
        categoryRvAdapter = CategoryRvAdapter(foodCategories) {
            getFoodList(it.strCategory)
        }
        binding.rvFoodCategory.setHasFixedSize(true)
        binding.rvFoodCategory.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvFoodCategory.adapter = categoryRvAdapter
    }

    private fun getFoodList(category: String) {
        lifecycleScope.launch {
            viewModel.getFoodList(category)
            viewModel.foodList.collect { state ->
                when (state){
                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> handleError(state.message.toString())
                    is ScreenState.Success ->{
                        if (state.data.isNullOrEmpty()) handleError(state.message.toString())
                        else displayFoodList(state.data)
                    }
                }
            }
        }
    }

    private fun displayFoodList(foodList: List<Food>) {
        foodRvAdapter = FoodRvAdapter(foodList, {}, {})
        binding.rvFood.setHasFixedSize(true)
        binding.rvFood.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvFood.adapter = foodRvAdapter
    }

    private fun handleError(msg: String) {
        binding.textViewError.visibility = View.VISIBLE
        binding.textViewError.text = msg
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        categoryRvAdapter = null
        foodRvAdapter = null
    }
}