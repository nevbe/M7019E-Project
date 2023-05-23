package com.ltu.m7019eblogapp.ui.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.ltu.m7019eblogapp.adapter.PostClickListener
import com.ltu.m7019eblogapp.adapter.PostListAdapter
import com.ltu.m7019eblogapp.data.util.DataFetchStatus
import com.ltu.m7019eblogapp.databinding.FragmentBrowseBinding

class BrowseFragment : Fragment() {

    private var _binding: FragmentBrowseBinding? = null

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

        _binding = FragmentBrowseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val browseViewModel =
            ViewModelProvider(this)[BrowseViewModel::class.java]

        val postListAdapter = PostListAdapter(
            PostClickListener { post ->
                browseViewModel.onPostListItemClicked(post)
            }
        )
        binding.postListRv.adapter = postListAdapter

        browseViewModel.postList.observe(viewLifecycleOwner) { postList ->
            postList?.let {
                postListAdapter.submitList(it)
            }
        }

        browseViewModel.navigateToPost.observe(viewLifecycleOwner) { post ->
            post?.let {
                println("From fragment: Navigating to ${post.title}")
                findNavController().navigate(
                    BrowseFragmentDirections.actionGlobalToNavigationSpecificPost(it)
                )
                browseViewModel.onPostNavigationComplete()
            }
        }

        browseViewModel.dataFetchStatus.observe(viewLifecycleOwner) { status ->
            val loadingAnim = binding.postListProgress

            when(status) {
                DataFetchStatus.LOADING -> loadingAnim.visibility = View.VISIBLE
                DataFetchStatus.DONE -> loadingAnim.visibility = View.GONE
                DataFetchStatus.ERROR -> {
                    loadingAnim.visibility = View.GONE
                    showSnackBar("Error loading posts...")
                }
                null -> {}
            }
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