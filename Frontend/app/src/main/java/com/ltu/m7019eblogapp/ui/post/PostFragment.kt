package com.ltu.m7019eblogapp.ui.post

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ltu.m7019eblogapp.R
import com.ltu.m7019eblogapp.databinding.FragmentLoginBinding
import com.ltu.m7019eblogapp.databinding.FragmentPostBinding
import com.ltu.m7019eblogapp.model.Post
import com.ltu.m7019eblogapp.ui.profile.ProfileFragmentDirections
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class PostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: PostViewModel
    private lateinit var post : Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Override back action, bottom destinations should act as nav root
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(PostFragmentDirections.actionNavigationSpecificPostToNavigationHost())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        println("Entering...")
        _binding = FragmentPostBinding.inflate(inflater)
        post = PostFragmentArgs.fromBundle(requireArguments()).post
        binding.post = post

        println(post)

        val date = post.created_at.removeSuffix("Z")
        val createdAt = LocalDateTime.parse(date)
        "${createdAt.hour}:${createdAt.minute}, ${createdAt.month} ${createdAt.dayOfMonth}, ${createdAt.year}".also { binding.tvPostCreatedAt.text = it }

        return binding.root
    }

}