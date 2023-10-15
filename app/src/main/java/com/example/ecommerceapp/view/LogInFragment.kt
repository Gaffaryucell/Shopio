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
import com.example.ecommerceapp.databinding.FragmentLogInBinding
import com.example.ecommerceapp.util.Status
import com.example.ecommerceapp.util.setupBottomSheetDialog
import com.example.ecommerceapp.viewmodel.LogInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private lateinit var binding : FragmentLogInBinding
    private lateinit var viewModel: LogInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogInBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(LogInViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLogIn.setOnClickListener {
            signIn()
        }
        binding.tvDontHaveAccount.setOnClickListener {
            val action = LogInFragmentDirections.actionLogInFragmentToRegisterFragment()
            Navigation.findNavController(it).navigate(action)
        }
        binding.tvForgotPasswordLogin.setOnClickListener {
            setupBottomSheetDialog { email ->
                viewModel.sendResetPasswordEmail(email,requireContext())
            }
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
                    if (it.message.equals("verify")){
                        println("verify asd")
                        Toast.makeText(requireContext(), "Verfy your email", Toast.LENGTH_SHORT).show()
                    }
                    binding.buttonLogIn.revertAnimation()
                }
                Status.LOADING -> {
                    binding.buttonLogIn.startAnimation()
                }
            }
        })
    }
    private fun signIn(){
        val email = binding.edEmailLogin.text.toString()
        val password = binding.edPasswordLogin.text.toString()
        viewModel.signIn(email,password)
    }
}