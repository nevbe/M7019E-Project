package com.ltu.m7019eblogapp.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.ltu.m7019eblogapp.R
import com.ltu.m7019eblogapp.databinding.FragmentProfileBinding
import com.ltu.m7019eblogapp.ui.login.LoginViewModel
import com.ltu.m7019eblogapp.ui.login.UserSessionViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel
    private lateinit var viewModelFactory : ProfileViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        //binding.lifecycleOwner = this
        val root : View = binding.root

        val userSession : UserSessionViewModel by activityViewModels()
        val application = requireNotNull(this.activity).application

        viewModelFactory = ProfileViewModelFactory(application, userSession.user!!)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        binding.loggedInUser = userSession.user!!

        //TODO: Use viewmodel and binding to populate info


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}