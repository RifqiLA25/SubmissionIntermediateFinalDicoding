package com.example.submissionawal

import com.example.submissionawal.data.remote.response.ListStory


object DataDummy {

    fun generateDummyStoryResponse(): List<ListStory> {
        val items: MutableList<ListStory> = arrayListOf()
        for (i in 1..100) {
            val story = ListStory(
                id = i.toString(),
                name = "name $i",
                description = "description $i",
                photoUrl = "photoUrl $i",
            )
            items.add(story)
        }
        return items
    }
}
