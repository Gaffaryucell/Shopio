package com.example.ecommerceapp.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentAccountBinding
import com.example.ecommerceapp.databinding.FragmentAccountoptionsBinding
import com.example.ecommerceapp.viewmodel.AccountViewModel
import com.example.ecommerceapp.viewmodel.AccountoptionsViewModel

class AccountFragment : Fragment() {

    private lateinit var viewModel: AccountViewModel
    private lateinit var binding : FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.gotocreateButton.setOnClickListener {
            val action = AccountFragmentDirections.actionNavigationAccountToCreateProductFragment()
            Navigation.findNavController(it).navigate(action)
        }
        // TODO: Use the ViewModel
    }

}