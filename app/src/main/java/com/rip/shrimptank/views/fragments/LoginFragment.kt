package com.rip.shrimptank.views.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.rip.shrimptank.R
import com.rip.shrimptank.databinding.FragmentLoginBinding
import com.rip.shrimptank.interactions.FragmentChangeListener
import com.rip.shrimptank.utils.UserInteractions
import com.rip.shrimptank.views.activities.MainActivity
import com.rip.shrimptank.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val authViewModel: AuthViewModel by viewModels()
    private var listener: FragmentChangeListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            listener = context
        } else {
            throw RuntimeException("Internal server error")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        binding.signinSignupText.setOnClickListener {
            listener?.navigateToFrag(R.id.action_loginFragment_to_registerFragment , R.id.loginFragment)
        }

        binding.loginBtn.setOnClickListener {

            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            UserInteractions.showLoading(requireActivity())
            authViewModel.validateAndLogin(email,password){ status,message->
                UserInteractions.hideLoad()
                if (status){
                    startActivity(Intent(requireContext(), MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }).apply {
                        requireActivity().finish()
                    }
                }
                else{
                    UserInteractions.showDlg(requireActivity(),message!!)
                }
            }
        }

        return binding.root
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}