package com.interview.diagnal.adapter

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.cursoradapter.widget.CursorAdapter
import com.interview.diagnal.R

/**
 * Created by Charles Raj I on 16/07/23.
 * @author Charles Raj I
 */
class SuggestionAdapter(context: Context, cursor: Cursor) : CursorAdapter(context, cursor, 0) {
    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val suggestionTextView = view.findViewById<TextView>(android.R.id.text1)
        suggestionTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
        suggestionTextView.text = cursor.getString(cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1))
    }

}

//class SuggestionAdapter(context: Context, suggestions: List<String>) :
//    ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, suggestions) {
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val view: View = convertView ?: LayoutInflater.from(context)
//            .inflate(android.R.layout.simple_list_item_1, parent, false)
//
//        val suggestionTextView = view.findViewById<TextView>(android.R.id.text1)
//        suggestionTextView.text = getItem(position)
//
//        return view
//    }
//}