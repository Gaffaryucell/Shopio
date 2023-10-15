package com.example.ecommerceapp.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentAccountoptionsBinding
import com.example.ecommerceapp.databinding.FragmentIntroductionBinding
import com.example.ecommerceapp.viewmodel.AccountoptionsViewModel
import com.example.ecommerceapp.viewmodel.IntroductionViewModel

class AccountoptionsFragment : Fragment() {


    private lateinit var viewModel: AccountoptionsViewModel
    private lateinit var binding : FragmentAccountoptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountoptionsBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(AccountoptionsViewModel::class.java)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLoginAccountOptions.setOnClickListener {
            val action = AccountoptionsFragmentDirections.actionAccountoptionsFragmentToLogInFragment()
            Navigation.findNavController(it).navigate(action)
        }
        binding.buttonRegisterAccountOptions.setOnClickListener {
            val action = AccountoptionsFragmentDirections.actionAccountoptionsFragmentToRegisterFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

}