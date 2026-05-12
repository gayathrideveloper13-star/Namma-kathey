package com.mindmatrix.nammakathey

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StoryAdapter(private var storyPages: List<String>) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    class StoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvStoryContent: TextView = view.findViewById(R.id.tvStoryContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_story_page, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.tvStoryContent.text = storyPages[position]
    }

    override fun getItemCount() = storyPages.size

    fun updateData(newPages: List<String>) {
        storyPages = newPages
        notifyDataSetChanged()
    }
}
