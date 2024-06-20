package com.dicoding.dapurnusantara.ui.home

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.dicoding.dapurnusantara.UserPreferences
import com.dicoding.dapurnusantara.ui.ViewModelFactory
import com.dicoding.dapurnusantara.ui.login.UserLoginViewModel
import com.dicoding.dapurnusantara.ui.login.dataStore
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.dapurnusantara.R
import com.dicoding.dapurnusantara.adapter.FoodAdapter
import com.dicoding.dapurnusantara.databinding.FragmentHomeBinding
import com.dicoding.dapurnusantara.dataclass.FoodItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private lateinit var viewModel: UserLoginViewModel
    private lateinit var tvHomeUsername: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodAdapter
    private lateinit var foodList: MutableList<FoodItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.HomeRV)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Load food data and set adapter
        foodList = loadFoodData()
        adapter = FoodAdapter(foodList) { position -> deleteFoodData(position) }
        recyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(requireContext().dataStore))
        ).get(UserLoginViewModel::class.java)

        // Bind TextView
        tvHomeUsername = view.findViewById(R.id.HomeUsername)

        // Observe username changes
        viewModel.getName().observe(viewLifecycleOwner) { username ->
            tvHomeUsername.text = username
        }
    }

    private fun loadFoodData(): MutableList<FoodItem> {
        val sharedPreferences = requireActivity().getSharedPreferences("food_data", Context.MODE_PRIVATE)
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

            val sharedPreferences = requireActivity().getSharedPreferences("food_data", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val foodListSet = foodList.map { "${it.imageUri}|${it.name}|${it.calories}" }.toMutableSet()

            editor.putStringSet("food_list", foodListSet)
            editor.apply()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

