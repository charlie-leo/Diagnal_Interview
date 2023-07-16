package com.interview.diagnal.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.interview.diagnal.R
import com.interview.diagnal.data.ContentItem
import com.interview.diagnal.databinding.MovieItemBinding
import java.util.Locale

/**
 * Created by Charles Raj I on 09/07/23.
 * @author Charles Raj I
 */
class MovieAdapter(private var content: MutableList<ContentItem> = mutableListOf()) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    /**
     * Method use to change the movie list in the recyclerview
     * @param item new movie list
     */
    fun setContent(item: MutableList<ContentItem>){
        if (content != item) {
            content = item
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val movieItem: MovieItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.movie_item,parent,false)
        return MovieViewHolder(movieItem)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.onBind(content[position])
    }

    class MovieViewHolder(private val movieItem: MovieItemBinding) : RecyclerView.ViewHolder(movieItem.root) {

        fun onBind(contentItem: ContentItem) {
            movieItem.contentItem = contentItem
            movieItem.movieName.isSelected = true
        }

    }
}