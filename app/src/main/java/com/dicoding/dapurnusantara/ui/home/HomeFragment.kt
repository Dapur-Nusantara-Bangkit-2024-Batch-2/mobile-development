package com.dicoding.dapurnusantara.ui.home

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.dicoding.dapurnusantara.UserPreferences
import com.dicoding.dapurnusantara.ui.ViewModelFactory
import com.dicoding.dapurnusantara.ui.login.UserLoginViewModel
import com.dicoding.dapurnusantara.ui.login.dataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dapurnusantara.adapter.FoodAdapter
import com.dicoding.dapurnusantara.databinding.FragmentHomeBinding
import com.dicoding.dapurnusantara.dataclass.FoodItem

class HomeFragment : Fragment() {
    private lateinit var viewModel: UserLoginViewModel
    private lateinit var adapter: FoodAdapter
    private lateinit var foodList: MutableList<FoodItem>
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(requireContext().dataStore))
        )[UserLoginViewModel::class.java]

        binding.HomeRV.layoutManager = LinearLayoutManager(context)

        foodList = loadFoodData()
        adapter = FoodAdapter(foodList) { position -> deleteFoodData(position) }
        binding.HomeRV.adapter = adapter

        viewModel.getName().observe(viewLifecycleOwner) { username ->
            binding.HomeUsername.text = username
        }
    }

    private fun loadFoodData(): MutableList<FoodItem> {
        val sharedPreferences =
            requireActivity().getSharedPreferences("food_data", Context.MODE_PRIVATE)
        val foodListSet = sharedPreferences.getStringSet("food_list", mutableSetOf())
        val foodList = mutableListOf<FoodItem>()

        foodListSet?.forEach {
            val data = it.split("|")
            if (data.size == 3) {
                val foodItem = FoodItem(Uri.parse(data[0]), data[1], data[2])
                foodList.add(foodItem)
            }
        }

        return foodList
    }

    private fun deleteFoodData(position: Int) {
        // Ensure position is valid
        if (position in foodList.indices) {
            foodList.removeAt(position)
            adapter.notifyItemRemoved(position)
            adapter.notifyItemRangeChanged(position, foodList.size)

            val sharedPreferences =
                requireActivity().getSharedPreferences("food_data", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val foodListSet =
                foodList.map { "${it.imageUri}|${it.name}|${it.calories}" }.toMutableSet()

            editor.putStringSet("food_list", foodListSet)
            editor.apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

