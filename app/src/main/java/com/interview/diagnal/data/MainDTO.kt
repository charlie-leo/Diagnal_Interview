package com.interview.diagnal.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

/**
 * Created by Charles Raj I on 09/07/23.
 * @author Charles Raj I
 */
data class MainDTO(val id: Int = 0): BaseObservable(){

    @Bindable
    var content: MutableList<ContentItem> = mutableListOf<ContentItem>()
        set(value) {
            field = value
            notifyPropertyChanged(BR.content)
        }

    var searchList: MutableList<ContentItem> = mutableListOf<ContentItem>()

    /**
     * Method use to add the content in to main list to reflect in the adapter
     * @param contentList List need to add to the adapter
     */
    fun addContent(contentList: List<ContentItem>){
        content.addAll(content.size,contentList)
        notifyPropertyChanged(BR.content)
    }
}
