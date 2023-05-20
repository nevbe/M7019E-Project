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
import androidx.fragment.app.activityViewModels
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
import com.ltu.m7019eblogapp.model.Post
import com.ltu.m7019eblogapp.model.SubmittablePost
import com.ltu.m7019eblogapp.model.Tag
import com.ltu.m7019eblogapp.ui.login.UserSessionViewModel
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
    private var selectedCategory : Category? = null

    private val userSession : UserSessionViewModel by activityViewModels()

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

        binding.titleCreate.editText?.addTextChangedListener {
            if(it.toString().trim().isNotBlank()){
                binding.titleCreate.error = null
            }
        }
        binding.contentCreate.editText?.addTextChangedListener {
            if(it.toString().trim().isNotBlank()){
                binding.contentCreate.error = null
            }
        }
        binding.mediaCreate.editText?.addTextChangedListener {
            if(it.toString().trim().isNotBlank()){
                binding.mediaCreate.error = null
            }
        }

        initCategoryAutoFill()

        initTagAutoFill()

        val submitBtn = binding.submitCreate
        submitBtn.setOnClickListener { submitPost() }

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

    private fun initCategoryAutoFill(){
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
            // Handle the selected category
            selectedCategory = parent.getItemAtPosition(position) as Category
            binding.categoryCreate.error = null
            println("USER SELECTED ${selectedCategory!!.name} AS CATEGORY")
        }

        categorySelectView?.addTextChangedListener {
            val query = it.toString().trim()
            if (query.isNotBlank()) {
                binding.categoryCreate.error = null
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
            } else {
                selectedCategory = null
                categorySelectView.clearListSelection()
            }

        }



    }

    private fun initTagAutoFill(){
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
                binding.tagCreate.error = "Tag already selected"
            } else {
                binding.tagCreate.error = null
                addChip(selectedTag)
            }
            tagSelectView.setText("")
            println("Selected tags: ${selectedTags.keys.toList()}")
        }

        tagSelectView?.addTextChangedListener {
            val query = it.toString().trim()

            if (query.isNotBlank() && query != "") {
                binding.tagCreate.error = null
                // Fetch tags based on the search query
                lifecycleScope.launch {
                    try {
                        val searchObject = TagSearchBody(Tag(name=query))
                        val tags = _container.blogRepository.searchTag(searchObject)
                        // TODO: We could clear here for scalability, but this is a mini-project :)

                        tagAdapter.addAll(tags)
                        tagAdapter.notifyDataSetChanged()

                    } catch (e: Exception) {
                        // Handle API request failure
                        println(e.message.toString())
                    }
                }
            }
        }
    }

    private fun submitPost(){
        val title = binding.titleCreate.editText?.text.toString()
        val content = binding.contentCreate.editText?.text.toString()
        val media = binding.mediaCreate.editText?.text.toString()

        val tagIDs : MutableList<String> = mutableListOf()

        for(tag in selectedTags){
            tagIDs.add(tag.value)
        }

        if(title != "" && content != "" && media != "" && tagIDs.isNotEmpty() && selectedCategory != null) {
            val post = SubmittablePost(
                title, content, selectedCategory!!.id, tagIDs,media
            )

            println("POST SUBMITTED!")
            println(post)

            lifecycleScope.launch {
                _container.blogRepository.createPost(userSession.accessToken!!,post)
            }
        } else {
            if (title == "") {
                binding.titleCreate.error = "Please enter a title"
            } else {
                binding.titleCreate.error = null
            }
            //--
            if (content == "") {
                binding.contentCreate.error = "Please enter content"
            } else {
                binding.contentCreate.error = null
            }
            //--
            if (media == "") {
                binding.mediaCreate.error = "Please enter media"
            } else {
                binding.mediaCreate.error = null
            }
            //--
            if (tagIDs.isEmpty()) {
                binding.tagCreate.error = "Please add at least one tag"
            } else {
                binding.tagCreate.error = null
            }
            //--
            if (selectedCategory == null) {
                binding.categoryCreate.error = "Please select a category"
            } else {
                binding.categoryCreate.error = null
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}