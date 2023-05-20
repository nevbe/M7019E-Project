package com.ltu.m7019eblogapp.ui.createpost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.MultiAutoCompleteTextView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout
import com.ltu.m7019eblogapp.R
import com.ltu.m7019eblogapp.databinding.FragmentCreatePostBinding
import com.ltu.m7019eblogapp.model.Tag

class CreatePostFragment : Fragment() {

    private var _binding: FragmentCreatePostBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val selectedTags = ArrayList<String>()

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
        val createPostViewModel =
            ViewModelProvider(this).get(CreatePostViewModel::class.java)

        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val categories = listOf("Cat", "Bat", "At", "Fat")
        val tags = listOf("Tag", "Bag", "Mag", "Lag")

        val categoryAdapter = ArrayAdapter(requireContext(), R.layout.fragment_create_post_list_item, categories)
        val tagAdapter = ArrayAdapter(requireContext(), R.layout.fragment_create_post_list_item, tags)

        val categorySelectView = binding.categoryCreate.editText as? AutoCompleteTextView
        categorySelectView?.setAdapter(categoryAdapter)

        val tagSelectView = (binding.tagCreate.editText as? AutoCompleteTextView)
        tagSelectView?.setAdapter(tagAdapter)

        tagSelectView?.setOnItemClickListener { parent, _, position, _ ->
            val selectedTag = parent.getItemAtPosition(position) as String
            if(selectedTags.contains(selectedTag)){
                val parentView = parent.parent as? TextInputLayout
                parentView?.error = "Tag already selected"
            } else {
                addChip(selectedTag)
            }
            tagSelectView.setText("")
        }


        return root
    }

    // --- Multiple tag select helper functions ----
    // Inspired by
    // https://github.com/krrishnaaaa/ChipDemo

    private fun addChip(tagName: String){
        selectedTags.add(tagName)
        binding.selectedTagsCreate.addView(getChip(tagName))
    }

    private fun getChip(tagName: String) : Chip {
        return Chip(requireContext()).apply {
            text = tagName
            isCloseIconVisible = true
            setOnCloseIconClickListener{
                selectedTags.remove((it as Chip).text)
                (it.parent as ChipGroup).removeView(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}