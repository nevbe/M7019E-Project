package com.ltu.m7019eblogapp.ui.createpost

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.MultiAutoCompleteTextView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout
import com.ltu.m7019eblogapp.R
import com.ltu.m7019eblogapp.data.DefaultAppContainer
import com.ltu.m7019eblogapp.data.service.CategorySearchBody
import com.ltu.m7019eblogapp.data.service.TagSearchBody
import com.ltu.m7019eblogapp.data.util.DataFetchStatus
import com.ltu.m7019eblogapp.databinding.FragmentCreatePostBinding
import com.ltu.m7019eblogapp.model.Category
import com.ltu.m7019eblogapp.model.Tag
import kotlinx.coroutines.launch

class CreatePostFragment : Fragment() {

    private var _binding: FragmentCreatePostBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val _container = DefaultAppContainer()

    // Selected tags, key of each tag is THE NAME, not the ID
    // TODO: Not a scalable sollution, but suitable for a mini-project
    private val selectedTags = HashMap<String, String>()

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
        var selectedCategory : Category

        // =========================================================================================
        // ---------------------------- CATEGORY AUTOFILL ------------------------------------------
        // =========================================================================================

        val initialCategories : MutableList<Category> = mutableListOf()
        lifecycleScope.launch {
            initialCategories.addAll(_container.blogRepository.getCategories(1))
        }

        val categoryAdapter = ArrayAdapter(requireContext(), R.layout.fragment_create_post_list_item, initialCategories)

        val categorySelectView = binding.categoryCreate.editText as? AutoCompleteTextView
        categorySelectView?.setAdapter(categoryAdapter)

        categorySelectView?.setOnItemClickListener { parent, _, position, _ ->
            selectedCategory = parent.getItemAtPosition(position) as Category
            println("USER SELECTED ${selectedCategory.name} AS CATEGORY")
            // Handle the selected category
        }

        categorySelectView?.addTextChangedListener {
            val query = it.toString().trim()
            if (query.isNotBlank()) {
                // Fetch categories based on the search query
                lifecycleScope.launch {
                    try {
                        val searchObject = CategorySearchBody(Category(name=query))
                        val categories = _container.blogRepository.searchCategory(searchObject)
                        // TODO: We could clear here for scalability, but this is a mini-project :)
                        categoryAdapter.addAll(categories)
                        categoryAdapter.notifyDataSetChanged()

                    } catch (e: Exception) {
                        // Handle API request failure
                        println(e.message.toString())
                    }
                }
            }
        }

        // ========================================================================================
        // -------------------------------- TAG AUTOFILL ------------------------------------------
        // ========================================================================================

        val initialTags : MutableList<Tag> = mutableListOf()
        lifecycleScope.launch {
            initialTags.addAll(_container.blogRepository.getTags(1))
        }

        val tagAdapter = ArrayAdapter(requireContext(), R.layout.fragment_create_post_list_item, initialTags)

        val tagSelectView = (binding.tagCreate.editText as? AutoCompleteTextView)
        tagSelectView?.setAdapter(tagAdapter)

        tagSelectView?.setOnItemClickListener { parent, _, position, _ ->
            val selectedTag = parent.getItemAtPosition(position) as Tag
            if(selectedTags[selectedTag.name] != null){
                val parentView = parent.parent as? TextInputLayout
                parentView?.error = "Tag already selected"
            } else {
                addChip(selectedTag)
            }
            tagSelectView.setText("")
            println("Selected tags: ${selectedTags.keys.toList()}")
        }

        tagSelectView?.addTextChangedListener {
            val query = it.toString().trim()

            if (query.isNotBlank() && query != "") {
                // Fetch categories based on the search query
                lifecycleScope.launch {
                    try {
                        val searchObject = TagSearchBody(Tag(name=query))
                        val tags = _container.blogRepository.searchTag(searchObject)
                        // TODO: We could clear here for scalability, but this is a mini-project :)
                        tagAdapter.clear()
                        tagAdapter.addAll(tags)
                        tagAdapter.notifyDataSetChanged()

                    } catch (e: Exception) {
                        // Handle API request failure
                        println(e.message.toString())
                    }
                }
            }
        }


        return root
    }

    // --- Multiple tag select helper functions ----
    // Inspired by
    // https://github.com/krrishnaaaa/ChipDemo

    private fun addChip(tag: Tag){
        selectedTags[tag.name] = tag.id
        binding.selectedTagsCreate.addView(getChip(tag))
    }

    private fun getChip(tag: Tag) : Chip {
        return Chip(requireContext()).apply {
            text = tag.name
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