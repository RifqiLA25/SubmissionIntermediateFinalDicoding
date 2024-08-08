package com.example.submissionawal.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionawal.R
import com.example.submissionawal.data.remote.response.ListStory
import com.example.submissionawal.databinding.ListStoryBinding
import com.example.submissionawal.ui.view.DetailStoryActivity

class ListAdapterStory : PagingDataAdapter<ListStory, ListAdapterStory.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class MyViewHolder(private val binding: ListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleText: TextView = itemView.findViewById(R.id.title)
        val descTextView: TextView = itemView.findViewById(R.id.descStory)
        fun bind(story: ListStory) {
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(imageView)
            titleText.text = story.name
            descTextView.text = story.description

            itemView.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(titleText, "name"),
                        Pair(imageView, "image"),
                        Pair(descTextView, "description")
                    )

                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra("name", story.name)
                intent.putExtra("image", story.photoUrl)
                intent.putExtra("description", story.description)
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStory>() {
            override fun areItemsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem == newItem
            }
        }
    }
}
