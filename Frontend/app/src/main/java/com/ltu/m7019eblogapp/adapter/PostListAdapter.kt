package com.ltu.m7019eblogapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ltu.m7019eblogapp.databinding.PostPreviewCardBinding
import com.ltu.m7019eblogapp.model.Post

class PostListAdapter(private val postClickListener: PostClickListener) : ListAdapter<Post, PostListAdapter.ViewHolder>(PostListDiffCallback()) {
    class ViewHolder(private var binding: PostPreviewCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post, postClickListener: PostClickListener){
            binding.post = post
            binding.clickListener = postClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PostPreviewCardBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), postClickListener)
    }
}

class PostListDiffCallback : DiffUtil.ItemCallback<Post>(){

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}

class PostClickListener(val clickListener: (post: Post) -> Unit) {
    fun onClick(post: Post) = clickListener(post)
}