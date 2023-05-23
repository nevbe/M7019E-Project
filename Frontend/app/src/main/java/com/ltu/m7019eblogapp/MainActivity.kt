package com.ltu.m7019eblogapp

import android.os.Bundle
import android.text.Layout.Directions
import android.view.Menu
import android.view.MenuItem
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.ltu.m7019eblogapp.adapter.ViewPagerAdapter
import com.ltu.m7019eblogapp.databinding.ActivityMainBinding
import com.ltu.m7019eblogapp.model.User
import com.ltu.m7019eblogapp.ui.ViewPagerHostFragmentDirections
import com.ltu.m7019eblogapp.ui.browse.BrowseFragment
import com.ltu.m7019eblogapp.ui.createpost.CreatePostFragment
import com.ltu.m7019eblogapp.ui.faq.FaqFragmentDirections
import com.ltu.m7019eblogapp.ui.home.HomeFragment
import com.ltu.m7019eblogapp.ui.profile.ProfileFragmentDirections

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var cachedUser: User? = null

    //private val loginViewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.topAppBar))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val config = AppBarConfiguration(setOf(R.id.navigation_host))
        setupActionBarWithNavController(navController,config)



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
            val profileItem : MenuItem = menu.findItem(R.id.navigation_profile)
            //Grab image view inside of said item
            val profilePicView : ShapeableImageView? = profileItem.actionView?.findViewById(R.id.profile_pic_header_view)

            Glide.with(this)
                .load(cachedUser!!.profilePicture) // image url
                .override(85,85) // Set image size
                .into(profilePicView!!);  // imageview object

            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
            val navController = navHostFragment.navController

            profilePicView.setOnClickListener {
                println("Backstackentry: ${navController.currentBackStackEntry?.destination}")
                navController.navigate(ProfileFragmentDirections.navigationGlobalToProfile())
            }

            val faqItem : MenuItem = menu.findItem(R.id.navigation_faq)
            faqItem.setOnMenuItemClickListener {
                println("Backstackentry: ${navController.currentBackStackEntry?.destination}")
                navController.navigate(FaqFragmentDirections.navigationGlobalToFaq())

                true
            }

        }



        return super.onCreateOptionsMenu(menu)
    }

    fun enableUI(user: User){
        binding.navView.visibility = View.VISIBLE
        binding.topAppBar.visibility = View.VISIBLE

        cachedUser = user
        binding.loggedInUser = cachedUser

        invalidateOptionsMenu() // Rebuild menu in order to call onCreateOptionsMenu
    }

    fun disableUI(){
        binding.navView.visibility = View.GONE
        binding.topAppBar.visibility = View.GONE

        cachedUser = null
        binding.loggedInUser = null
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