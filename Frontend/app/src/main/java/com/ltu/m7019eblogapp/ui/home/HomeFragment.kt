package com.ltu.m7019eblogapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import androidx.navigation.fragment.DialogFragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.sidesheet.SideSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.ltu.m7019eblogapp.R
import com.ltu.m7019eblogapp.adapter.PostClickListener
import com.ltu.m7019eblogapp.adapter.PostListAdapter
import com.ltu.m7019eblogapp.data.util.DataFetchStatus
import com.ltu.m7019eblogapp.databinding.FragmentHomeBinding
import com.ltu.m7019eblogapp.model.Category
import com.ltu.m7019eblogapp.model.Tag
import com.ltu.m7019eblogapp.ui.createpost.CreatePostFragment

class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val selectedTags : MutableList<Tag> = mutableListOf()
    private var selectedCategory : Category? = null

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
            CreatePostFragment().show(parentFragmentManager, "Create Post")
        }

        val sideSheetDialog = SideSheetDialog(requireContext())
        sideSheetDialog.setContentView(R.layout.filter_side_sheet)

        val button = sideSheetDialog.findViewById<Button>(R.id.sidesheet_button)
        button?.setOnClickListener {
            homeViewModel.updatePostsWithFilter(selectedCategory,selectedTags)
            sideSheetDialog.hide()
        }

        val resetButton = sideSheetDialog.findViewById<Button>(R.id.sidesheet_button_reset)
        resetButton?.setOnClickListener {
            homeViewModel.getPosts()
            selectedTags.clear()
            selectedCategory = null

            val chipGropView : ChipGroup? = sideSheetDialog.findViewById(R.id.sheet_tag_chipGroup)
            chipGropView?.clearCheck()

            val categorySelectView = sideSheetDialog.findViewById(R.id.sheet_category_field) as? AutoCompleteTextView
            categorySelectView?.clearListSelection()
            categorySelectView?.text?.clear()

            sideSheetDialog.hide()
        }

        homeViewModel.tagList.observe(viewLifecycleOwner) { tagList ->
            tagList?.let{
                addChips(tagList, sideSheetDialog)
            }
        }

        homeViewModel.categoryList.observe(viewLifecycleOwner) { categories ->
            categories?.let {
                addCategories(categories, sideSheetDialog)
            }
        }


        binding.homeBtnFilter.setOnClickListener{
            sideSheetDialog.show()
        }


        return root
    }

    private fun addChips(tags: List<Tag>, dialog: SideSheetDialog){
        val chipGropView : ChipGroup? = dialog.findViewById(R.id.sheet_tag_chipGroup)

        chipGropView?.let {
            for(tag in tags){
                println("Adding tag")
                chipGropView.addView(getChip(tag))
            }
        }

    }

    private fun getChip(tag: Tag) : Chip {
        return Chip(requireContext()).apply {
            text = tag.name
            isCheckable = true
            setOnCheckedChangeListener { _, checked ->
                if(!checked){
                    selectedTags.remove(tag)
                    println("Selected tags: $selectedTags")
                } else {
                    selectedTags.add(tag)
                    println("Selected tags: $selectedTags")
                }
            }
        }
    }

    private fun addCategories(categories: List<Category>, dialog: SideSheetDialog){
        val categoryAdapter = ArrayAdapter(requireContext(), R.layout.fragment_create_post_list_item, categories)
        val categorySelectView = dialog.findViewById(R.id.sheet_category_field) as? AutoCompleteTextView

        categorySelectView?.setAdapter(categoryAdapter)

        categorySelectView?.setOnItemClickListener { parent, _, position, _ ->
            // Handle the selected category
            selectedCategory = parent.getItemAtPosition(position) as Category
            println("USER SELECTED ${selectedCategory!!.name} AS CATEGORY")
        }
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
