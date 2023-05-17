package com.ltu.m7019eblogapp

import android.opengl.Visibility
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View

import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.ltu.m7019eblogapp.data.util.DataFetchStatus
import com.ltu.m7019eblogapp.databinding.ActivityMainBinding
import com.ltu.m7019eblogapp.model.User
import com.ltu.m7019eblogapp.ui.login.LoginViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var cachedUser: User? = null

    private val loginViewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.topAppBar))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        val navView = binding.navView
        navView.setupWithNavController(navController)

        /*
        TODO: enableUI is called from loginfragment, which is kind of a hack

        loginViewModel.loggedInUser.observe(this) { status ->
            status?.let {
                cachedUser = it
                println("Enabling UI...")
                println("User: $cachedUser")
                enableUI()
            }
        }

         */
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_header_menu,menu)

        if(cachedUser != null && menu != null){
            //Grab menu item
            val profileItem : MenuItem = menu.findItem(R.id.header_menu_profile_item)
            //Grab image view inside of said item
            val profilePicView : ShapeableImageView? = profileItem.actionView?.findViewById(R.id.profile_pic_header_view)

            Glide.with(this)
                .load(cachedUser!!.profilePicture) // image url
                .override(85,85) // Set image size
                .into(profilePicView!!);  // imageview object

            //TODO: Logout knapp

        }

        return super.onCreateOptionsMenu(menu)
    }

    fun enableUI(user: User){
        binding.navView.visibility = View.VISIBLE
        binding.topAppBar.visibility = View.VISIBLE

        cachedUser = user

        invalidateOptionsMenu() // Rebuild menu in order to call onCreateOptionsMenu
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