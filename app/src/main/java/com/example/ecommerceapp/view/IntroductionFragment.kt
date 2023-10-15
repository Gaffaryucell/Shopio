package com.example.ecommerceapp.view

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentIntroductionBinding
import com.example.ecommerceapp.databinding.FragmentLogInBinding
import com.example.ecommerceapp.util.Status
import com.example.ecommerceapp.viewmodel.IntroductionViewModel
import com.example.ecommerceapp.viewmodel.LogInViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroductionFragment : Fragment() {

    private lateinit var viewModel: IntroductionViewModel
    private lateinit var binding : FragmentIntroductionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroductionBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(IntroductionViewModel::class.java)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonStart.setOnClickListener {
            val action = IntroductionFragmentDirections.actionIntroductionFragmentToAccountoptionsFragment()
            Navigation.findNavController(it).navigate(action)
        }
        observeLiveData()
    }
    private fun observeLiveData() {
        viewModel.authState.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    val intent = Intent(requireContext(),ShoppingActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)
                    requireActivity().finish()
                    requireContext().startActivity(intent)
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        })
    }

}