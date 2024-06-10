package com.example.myappgithub.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myappgithub.data.response.ItemsItem
import com.example.myappgithub.databinding.ItemRowBinding

class UserAdapter : ListAdapter<ItemsItem, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var onUserItemClickListener: OnUserItemClickListener? = null
    var listener: RecyclerViewClickListener? = null

    fun setOnUserItemClickListener(listener: OnUserItemClickListener) {
        this.onUserItemClickListener = listener
    }

    fun setRecyclerViewClickListener(listener: RecyclerViewClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.MyViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(it, user)
        }
    }

    inner class MyViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: ItemsItem) {
            binding.tvUsername.text = user.login
            Glide.with(binding.root.context)
                .load(user.avatarUrl)
                .into(binding.imageAva)
        }
    }

    interface OnUserItemClickListener {
        fun onUserItemClick(user: ItemsItem)
    }

    interface RecyclerViewClickListener {
        fun onItemClick(view: View, item: ItemsItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
