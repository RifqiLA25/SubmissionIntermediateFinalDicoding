package com.example.submissionawal.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.submissionawal.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        getDetail()
    }

    private fun getDetail() {
        val storyName = intent.getStringExtra("name")
        val imageStory = intent.getStringExtra("image")
        val descStory = intent.getStringExtra("description")

        Glide.with(applicationContext)
            .load(imageStory)
            .transform(RoundedCorners(16))
            .into(binding.imageView)

        binding.title.text = storyName
        binding.descStory.text = descStory
    }

}