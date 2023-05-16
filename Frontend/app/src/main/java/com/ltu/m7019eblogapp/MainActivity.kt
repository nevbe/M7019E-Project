package com.ltu.m7019eblogapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.ltu.m7019eblogapp.data.DefaultAppContainer
import com.ltu.m7019eblogapp.databinding.ActivityMainBinding
import com.ltu.m7019eblogapp.model.User
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val _container : DefaultAppContainer = DefaultAppContainer()

    private lateinit var account: Auth0
    private lateinit var manager: CredentialsManager

    private var cachedCredentials: Credentials? = null
    private var cachedUserProfile: UserProfile? = null
    private var cachedUser : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )

        val apiClient = AuthenticationAPIClient(account)
        manager = CredentialsManager(apiClient, SharedPreferencesStorage(this))

        binding = ActivityMainBinding.inflate(layoutInflater)

        if(manager.hasValidCredentials()){

            manager.getCredentials(object: Callback<Credentials, CredentialsManagerException> {
                override fun onSuccess(result: Credentials) {
                    // Use credentials
                    cachedCredentials = result
                    println("credentials grabbed!")
                    showUserProfile()
                }

                override fun onFailure(error: CredentialsManagerException) {
                    // No credentials were previously saved or they couldn't be refreshed
                    showSnackBar("Failed to refresh credentials, redirecting...")
                    loginWithBrowser()
                }
            })

        } else {
            loginWithBrowser()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_header_menu,menu)

        if(cachedUserProfile != null && menu != null){
            //Grab menu item
            val profileItem : MenuItem = menu.findItem(R.id.header_menu_profile_item)
            //Grab image view inside of said item
            val profilePicView : ShapeableImageView? = profileItem.actionView?.findViewById(R.id.profile_pic_header_view)

            Glide.with(this)
                .load(cachedUserProfile!!.pictureURL) // image url
                .override(85,85) // Set image size
                .into(profilePicView!!);  // imageview object
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun enableUI(){
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.topAppBar))
        invalidateOptionsMenu() // Rebuild menu in order to call onCreateOptionsMenu

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)
    }

    private fun loginWithBrowser(){
        WebAuthProvider
            .login(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .withScope(getString(R.string.login_scopes))
            .withAudience(getString(R.string.login_audience, getString(R.string.com_auth0_domain)))
            .start(this, object : Callback<Credentials, AuthenticationException> {

                override fun onFailure(error: AuthenticationException) {
                    showSnackBar(getString(R.string.login_failure_message, error.getCode()))
                }

                override fun onSuccess(result: Credentials) {
                    cachedCredentials = result
                    manager.saveCredentials(result)

                    showSnackBar(getString(R.string.login_success_message, result.accessToken))
                    showUserProfile()

                }
            })
    }

    private fun showUserProfile() {
        // Guard against showing the profile when no user is logged in
        if (cachedCredentials == null) {
            showSnackBar("No credentials available for user grab, redirecting...")
            loginWithBrowser()
            return
        }

        val client = AuthenticationAPIClient(account)
        client
            .userInfo(cachedCredentials!!.accessToken)
            .start(object : Callback<UserProfile, AuthenticationException> {

                override fun onFailure(error: AuthenticationException) {
                    showSnackBar(getString(R.string.general_failure_with_exception_code,
                        error.getCode()))
                }

                override fun onSuccess(result: UserProfile) {
                    cachedUserProfile = result
                    println("User profile grabbed!")
                    enableUI()

                    /*
                    runBlocking {
                        cachedUser = doLogin(cachedCredentials!!.accessToken, result.name!!)
                    }
                     */
                }

            })
    }

    private suspend fun doLogin(accessToken: String, name: String) : User? {
        return _container.blogRepository.login(accessToken, name)
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