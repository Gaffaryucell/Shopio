package com.example.ecommerceapp.view

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
import com.example.ecommerceapp.databinding.FragmentRegisterBinding
import com.example.ecommerceapp.util.Status
import com.example.ecommerceapp.viewmodel.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding : FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonRegisterRegister.setOnClickListener {
            signUp()
        }
        binding.tvDoYouHaveAccount.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLogInFragment()
            Navigation.findNavController(it).navigate(action)
        }
        observeLiveData()
    }
    private fun observeLiveData(){
        viewModel.authState.observe(viewLifecycleOwner,Observer{
            when(it.status){
                Status.SUCCESS->{
                    val action = RegisterFragmentDirections.actionRegisterFragmentToLogInFragment()
                    Navigation.findNavController(requireView()).navigate(action)
                }
                Status.ERROR->{
                    Toast.makeText(
                        requireContext(),
                        it.message ?: "error : try again later",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.buttonRegisterRegister.revertAnimation()
                }
                Status.LOADING->{
                    binding.buttonRegisterRegister.startAnimation()
                }
            }
        })
        viewModel.verification.observe(viewLifecycleOwner,Observer{
            when(it.status){
                Status.SUCCESS->{
                    Toast.makeText(
                        requireContext(),
                        "Verification e-mail sent please confirm your inbox ",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.signOut()
                }
                Status.ERROR->{
                    Toast.makeText(requireContext(), "Error1", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING->{

                }
            }
        })
    }
    private fun signUp(){
        val email = binding.edEmailRegister.text.toString()
        val firstName = binding.edFirstNameRegister.text.toString()
        val lastName = binding.edLastNameRegister.text.toString()
        val password = binding.edPasswordRegister.text.toString()
        viewModel.signUp(firstName,lastName,email,password)
    }
}