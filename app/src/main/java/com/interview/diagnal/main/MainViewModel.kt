package com.interview.diagnal.main

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.interview.diagnal.R
import com.interview.diagnal.adapter.MovieAdapter
import com.interview.diagnal.data.ContentItem
import com.interview.diagnal.data.MainDTO
import com.interview.diagnal.data.MovieResponse
import com.squareup.picasso.Picasso
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Charles Raj I on 08/07/23.
 * @author Charles Raj I
 */

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    var mainDTO: MainDTO = MainDTO()
    lateinit var movieAdapter: MovieAdapter

    /**
     * Method use to fetch all the movie list by pageNo
     * @param context application context
     * @param pageNo page number to fetch the particular page
     */
    fun getMovieList(context: Context, pageNo: Int){
        if (pageNo > 3){
            return
        }
        viewModelScope.launch {
            var movieData = "CONTENTLISTINGPAGE-PAGE1.json"
            if (pageNo ==1){
                movieData = "CONTENTLISTINGPAGE-PAGE1.json"
            } else if (pageNo == 2){
                movieData = "CONTENTLISTINGPAGE-PAGE2.json"
            } else if (pageNo == 3){
                movieData = "CONTENTLISTINGPAGE-PAGE3.json"
            }
            val fileInString: String =
                context.assets.open(movieData).bufferedReader().use { it.readText() }
            val movieResponse: MovieResponse = Gson().fromJson(fileInString,MovieResponse::class.java)

            if (pageNo ==1) {
                mainDTO.content =
                    (movieResponse.page?.contentItems?.content as MutableList<ContentItem>?)!!
            } else {
                mainDTO.addContent(
                    (movieResponse.page?.contentItems?.content as MutableList<ContentItem>?)!!)
                movieAdapter.notifyDataSetChanged()
            }
        }
    }

    fun getSearchMovieList(context: Context){
        viewModelScope.launch {
            val queryList = listOf<String>("CONTENTLISTINGPAGE-PAGE1.json","CONTENTLISTINGPAGE-PAGE2.json","CONTENTLISTINGPAGE-PAGE3.json")
            queryList.forEach{
                val fileInString: String =
                    context.assets.open(it).bufferedReader().use { it.readText() }
                val movieResponse: MovieResponse = Gson().fromJson(fileInString,MovieResponse::class.java)
                mainDTO.searchList.addAll(
                        (movieResponse.page?.contentItems?.content as MutableList<ContentItem>?)!!)
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    companion object {
        @BindingAdapter("loadImage")
        @JvmStatic fun ImageView.loadImage(imageName: String){
            val id =  this.resources.getIdentifier(imageName.split(".")[0],"drawable",this.context.packageName)
            Log.d("TAG", "loadImage: id : $id, name : $imageName, ide: ${imageName.split(".")[0]}" )
            if (id > 0) {
                Picasso.get().load(id).into(this)
            }
        }

    }

}