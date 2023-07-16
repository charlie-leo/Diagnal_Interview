package com.interview.diagnal.main

import android.app.SearchManager
import android.content.res.Configuration
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.interview.diagnal.R
import com.interview.diagnal.adapter.MovieAdapter
import com.interview.diagnal.adapter.SuggestionAdapter
import com.interview.diagnal.data.ContentItem
import com.interview.diagnal.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private val portraitDecoration = GridSpaceItemDecorator(3, 30, true);
    private val landscapeDecoration = GridSpaceItemDecorator(7, 30, true);
    private lateinit var activityMainBinding: ActivityMainBinding
    private var pageNo = 1
    private lateinit var suggestionAdapter: SuggestionAdapter
    private val nameSet = HashSet<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        setSupportActionBar(activityMainBinding.mainToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mainViewModel.getSearchMovieList(applicationContext)

        activityMainBinding.mainDto = mainViewModel.mainDTO
        mainViewModel.getMovieList(this, pageNo)
        pageNo += 1

        activityMainBinding.mainToolBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        mainViewModel.movieAdapter = MovieAdapter(mainViewModel.mainDTO.content)
        activityMainBinding.movieListRecyclerView.addItemDecoration(portraitDecoration)
        activityMainBinding.movieListRecyclerView.layoutManager =
            GridLayoutManager(applicationContext, 3)
        activityMainBinding.movieListRecyclerView.adapter = mainViewModel.movieAdapter
        activityMainBinding.movieListRecyclerView.addOnScrollListener(scrollListener())
    }

    /**
     * Method used in the recyclerview scroll listener to deduct the
     * last index then load the next page
     */
    private fun scrollListener() = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
            if ((linearLayoutManager != null)
                &&
                (linearLayoutManager.findLastCompletelyVisibleItemPosition() == (mainViewModel.mainDTO.content.size - 1))
            ) {
                mainViewModel.getMovieList(this@MainActivity, pageNo)
                pageNo += 1
            }
        }
    }

    /**
     * Override method to get the menu options
     * @param menu current option menu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.movie_menu, menu)
        val searchView = menu?.findItem(R.id.menu_screen_search)?.actionView as SearchView
        searchView.setOnQueryTextListener(searchQueryListener(searchView))
        suggestionAdapter =
            SuggestionAdapter(this,MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1)))
        searchView.suggestionsAdapter = suggestionAdapter
        searchView.setOnSuggestionListener(searchSuggestionListener(searchView))
        return true
    }

    /**
     * Method holding the search Text listener
     * @param searchView SearchView Context
     */
    private fun searchQueryListener(searchView: SearchView) = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            applyFilter(query)
            searchView.clearFocus()
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            applyFilter(newText)

            val suggestions = fetchSuggestions(newText)
            updateSuggestionsAdapter(suggestions)

            return true
        }
    }

    /**
     * Method is use to show the search result movies in the recyclerview
     * @param newText search keyword to filter the list
     */
    fun applyFilter(newText: String) {
        val text = newText.uppercase()
        if (text.trim().length >= 3) {
            val items = mainViewModel.mainDTO.searchList.filter {
                it.name?.lowercase(Locale.getDefault())?.contains(text.lowercase())!!
            }
            mainViewModel.movieAdapter.setContent(items as MutableList<ContentItem>)
        } else {
            mainViewModel.movieAdapter.setContent(mainViewModel.mainDTO.content)
        }
    }

    /**
     * Method holding the search suggestion listener
     * @param searchView SearchView Context
     */
    private fun searchSuggestionListener(searchView: SearchView) = object : SearchView.OnSuggestionListener {
        override fun onSuggestionSelect(position: Int): Boolean {
            // Handle suggestion selection
            return true
        }

        override fun onSuggestionClick(position: Int): Boolean {
            // Handle suggestion click
            val item = nameSet.toList().get(position)
            searchView.setQuery(item, false)
            return true
        }
    }

    /**
     * Method use to fetch suggestion item to the searchview
     * @param query Search Keyword
     */
    private fun fetchSuggestions(query: String): Cursor {
        val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))

        mainViewModel.mainDTO.searchList.mapNotNull { contentItem ->
            contentItem.name?.lowercase(Locale.getDefault())
        }.filter {
            it.contains(query.lowercase())
        }.filter {
            nameSet.add(it)
        }
        nameSet.forEachIndexed { index, item ->
            cursor.addRow(arrayOf(index, item))
        }
        return cursor
    }

    /**
     * Method use to update the search suggestion list
     * @param cursor Cursor contain suggestion items
     */
    private fun updateSuggestionsAdapter(cursor: Cursor) {
        suggestionAdapter.changeCursor(cursor)
    }

    /**
     * This override method is use to change the recyclerview grid layout
     * based on the orientation.
     * @param newConfig Configuration to change
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activityMainBinding.movieListRecyclerView.removeItemDecoration(portraitDecoration)
            activityMainBinding.movieListRecyclerView.addItemDecoration(landscapeDecoration)
            activityMainBinding.movieListRecyclerView.layoutManager =
                GridLayoutManager(applicationContext, 7)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            activityMainBinding.movieListRecyclerView.removeItemDecoration(landscapeDecoration)
            activityMainBinding.movieListRecyclerView.addItemDecoration(portraitDecoration)
            activityMainBinding.movieListRecyclerView.layoutManager =
                GridLayoutManager(applicationContext, 3)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        suggestionAdapter.changeCursor(null)
    }
}