package com.ltu.m7019eblogapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ltu.m7019eblogapp.R
import com.ltu.m7019eblogapp.adapter.ViewPagerAdapter
import com.ltu.m7019eblogapp.databinding.FragmentViewPagerHostBinding
import com.ltu.m7019eblogapp.ui.browse.BrowseFragment
import com.ltu.m7019eblogapp.ui.createpost.CreatePostFragment
import com.ltu.m7019eblogapp.ui.home.HomeFragment

class ViewPagerHostFragment : Fragment() {
    private var _binding : FragmentViewPagerHostBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentViewPagerHostBinding.inflate(inflater, container, false)

        val viewPager = binding.viewPager
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        val rootFragments = listOf(HomeFragment(), CreatePostFragment(), BrowseFragment())

        val adapter = ViewPagerAdapter(rootFragments, requireActivity().supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> viewPager.setCurrentItem(0, true)
                R.id.navigation_create -> viewPager.setCurrentItem(1, true)
                R.id.navigation_browse -> viewPager.setCurrentItem(2, true)
            }
            true
        }

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> bottomNavigationView.menu.findItem(R.id.navigation_home).isChecked = true
                    1 -> bottomNavigationView.menu.findItem(R.id.navigation_create).isChecked = true
                    2 -> bottomNavigationView.menu.findItem(R.id.navigation_browse).isChecked = true
                }
            }
        })


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}