package com.ltu.m7019eblogapp.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.ltu.m7019eblogapp.MainActivity
import com.ltu.m7019eblogapp.R
import com.ltu.m7019eblogapp.databinding.FragmentProfileBinding
import com.ltu.m7019eblogapp.ui.login.LoginFragmentDirections
import com.ltu.m7019eblogapp.ui.login.LoginViewModel
import com.ltu.m7019eblogapp.ui.login.UserSessionViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel
    private lateinit var viewModelFactory : ProfileViewModelFactory

    private lateinit var account: Auth0
    private lateinit var manager: CredentialsManager

    private val userSession : UserSessionViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        //binding.lifecycleOwner = this
        val root : View = binding.root

        // val userSession : UserSessionViewModel by activityViewModels()
        val application = requireNotNull(this.activity).application

        viewModelFactory = ProfileViewModelFactory(application, userSession.user!!)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        binding.loggedInUser = userSession.user!!

        account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )

        val apiClient = AuthenticationAPIClient(account)
        manager = CredentialsManager(apiClient, SharedPreferencesStorage(this.requireContext()))

        Glide.with(this)
            .load(userSession.user!!.profilePicture) // image url
            .override(200,200) // Set image size
            .into(binding.profileUserPic);  // imageview object

        binding.profileButtonLogout.setOnClickListener { doLogout() }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //activity?.supportFragmentManager?.popBackStack()
        _binding = null
    }

    //TODO: Move to util?
    /**
     * Initiate AUTH0 logout flow
     */
    fun doLogout(){
        WebAuthProvider.logout(account).withScheme(getString(R.string.com_auth0_scheme))
            .start(this.requireContext(), object : Callback<Void?, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    showSnackBar(getString(R.string.general_failure_with_exception_code,
                        error.getCode()))
                }

                /**
                 * Method called on success with the result.
                 *
                 * @param result Request result
                 */
                override fun onSuccess(result: Void?) {
                    finalizeLogout()
                }
            })
    }

    //TODO: Move to util?
    /**
     * Finalize logout and reset cache
     * - SHOULD ONLY BE USED AS A CALLBACK FROM doLogout()
     */
    private fun finalizeLogout(){
        manager.clearCredentials()

        userSession.user = null
        userSession.accessToken = null
        binding.loggedInUser = null

        (activity as MainActivity).disableUI()

        this.findNavController().navigate(
            ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
    }

    private fun showSnackBar(text: String) {
        Snackbar
            .make(
                binding.root,
                text,
                Snackbar.LENGTH_LONG
            ).show()
    }
    
    
    
}