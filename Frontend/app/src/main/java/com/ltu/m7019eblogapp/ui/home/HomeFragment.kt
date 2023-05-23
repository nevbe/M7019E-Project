package com.ltu.m7019eblogapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ltu.m7019eblogapp.R
import com.ltu.m7019eblogapp.adapter.PostClickListener
import com.ltu.m7019eblogapp.adapter.PostListAdapter
import com.ltu.m7019eblogapp.data.util.DataFetchStatus
import com.ltu.m7019eblogapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Override back action, bottom destinations should act as nav root
        requireActivity().onBackPressedDispatcher.addCallback(this) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        val postListAdapter = PostListAdapter(
            PostClickListener { post ->
                homeViewModel.onPostListItemClicked(post)
            }
        )
        binding.postListRv.adapter = postListAdapter

        homeViewModel.postList.observe(viewLifecycleOwner) { postList ->
            postList?.let {
                postListAdapter.submitList(it)
            }
        }

        homeViewModel.navigateToPost.observe(viewLifecycleOwner) { post ->
            post?.let {
                println("From fragment: Navigating to ${post.title}")
                findNavController().navigate(
                    HomeFragmentDirections.actionGlobalToNavigationSpecificPost(it)
                )
                homeViewModel.onPostNavigationComplete()
            }
        }

        homeViewModel.dataFetchStatus.observe(viewLifecycleOwner) { status ->
            val loadingAnim = binding.postListProgress

            when (status) {
                DataFetchStatus.LOADING -> loadingAnim.visibility = View.VISIBLE
                DataFetchStatus.DONE -> loadingAnim.visibility = View.GONE
                DataFetchStatus.ERROR -> {
                    loadingAnim.visibility = View.GONE
                    showSnackBar("Error loading posts...")
                }
                null -> {}
            }
        }

        binding.homeBtnCreate.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCreateFragment())
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
