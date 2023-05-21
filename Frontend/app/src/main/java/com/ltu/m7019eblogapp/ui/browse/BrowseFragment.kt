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
import com.ltu.m7019eblogapp.databinding.FragmentBrowseBinding

class BrowseFragment : Fragment() {

    private var _binding: FragmentBrowseBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Override back action, bottom destinations should act as nav root
        requireActivity().onBackPressedDispatcher.addCallback(this) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val browseViewModel =
            ViewModelProvider(this).get(BrowseViewModel::class.java)

        _binding = FragmentBrowseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        browseViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        browseViewModel.postList.observe(viewLifecycleOwner) { postList ->
            binding.buttonLatest.visibility = View.VISIBLE
            binding.buttonLatest.setOnClickListener {
                findNavController().navigate(BrowseFragmentDirections.actionNavigationBrowseToNavigationSpecificPost(postList[0]))
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}