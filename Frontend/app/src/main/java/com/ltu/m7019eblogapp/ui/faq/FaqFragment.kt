package com.ltu.m7019eblogapp.ui.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ltu.m7019eblogapp.databinding.FragmentFaqBinding


class FaqFragment : Fragment() {

    private var _binding: FragmentFaqBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFaqBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val listAdapter = FaqListAdapter(this.requireContext())
        binding.faqList.setAdapter(listAdapter)

        // Inflate the layout for this fragment
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}