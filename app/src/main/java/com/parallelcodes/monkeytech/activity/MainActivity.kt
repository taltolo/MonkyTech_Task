package com.parallelcodes.monkeytech.activity


import android.app.SearchManager
import android.content.res.Configuration
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parallelcodes.monkeytech.R

import com.parallelcodes.monkeytech.Utils.AsyncResponseTitle
import com.parallelcodes.monkeytech.Utils.BackgroundSound
import com.parallelcodes.monkeytech.Utils.GetGiphyAsyncTask
import com.parallelcodes.monkeytech.Utils.AsyncResponseGif
import com.parallelcodes.monkeytech.Utils.TitleTrendAsyncTask
import com.parallelcodes.monkeytech.api.ApiInterface
import com.parallelcodes.monkeytech.api.ServiceBuilder
import com.parallelcodes.monkeytech.models.GifAdapter
import com.parallelcodes.monkeytech.models.GiphySearchImagePoko



class MainActivity : AppCompatActivity() {
    private var LANDSCAPE:String = "LANDSCAPE"
    private var PORTRAIT : String = "PORTRAIT"
    private var queryToSave : String = ""
    private var recyclerView:RecyclerView ? = null
    private var gridLayoutManager:GridLayoutManager ? = null
    private var giphyAdapter:GifAdapter ? = null
    private var mBackgroundSound : BackgroundSound? = null
    private var FLAG : Boolean = false
    private var dataList : List<GiphySearchImagePoko.GiphySearchDatum> ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var gif_search= findViewById<SearchView>(R.id.gif_search)
        val request = ServiceBuilder.buildService(ApiInterface::class.java)

        gif_search.setSubmitButtonEnabled(true)

        var suggestions = listOf("")
        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.item_label)
        val cursorAdapter = SimpleCursorAdapter(this,
            R.layout.search_item, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        var titleFecher = TitleTrendAsyncTask(request,this, object : AsyncResponseTitle {
            override fun processTitleFinish(output: List<String>) {
                    suggestions= output

            }
        })

        titleFecher.execute(25)
        mBackgroundSound = BackgroundSound(applicationContext)
        gif_search.suggestionsAdapter=cursorAdapter
        gif_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                queryToSave=query
                getData(request,queryToSave)
                if(!mBackgroundSound?.isCancelled!!)
                     mBackgroundSound?.cancel(true)
                if(query.contains("pig")){
                    mBackgroundSound?.execute()
                }
                return false
            }
            override fun onQueryTextChange(query: String): Boolean {
                val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                query?.let {
                    suggestions.forEachIndexed { index, suggestion ->
                        if (suggestion.contains(query, true))
                            cursor.addRow(arrayOf(index, suggestion))
                    }
                }

                cursorAdapter.changeCursor(cursor)
                return true
            }
        })
        gif_search.setQueryHint("Search Your Favorite Gif")

        gif_search.setOnSuggestionListener(object: SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {

                val cursor = gif_search.suggestionsAdapter.getItem(position) as Cursor
                val selection = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                gif_search.setQuery(selection, false)

                // Do something with selection
                return true
            }

        })

        recyclerView = findViewById(R.id.gif_rv)
        var orientation=getOrientation()
        var gridNum= if (orientation == PORTRAIT) 2 else 4
        gridLayoutManager = GridLayoutManager(applicationContext, gridNum
        ,LinearLayoutManager.VERTICAL,false)
       recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        if(!FLAG)getData(request,"pig")
        getData(request,queryToSave)

    }

    override fun onResume() {
        super.onResume()
        if(queryToSave.contains("pig")){
            mBackgroundSound = BackgroundSound(applicationContext)
            mBackgroundSound?.execute()
        }

    }

    override fun onPause() {
        mBackgroundSound?.cancel(true)
        super.onPause()

    }

    fun getOrientation(): String {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return LANDSCAPE
        } else {
            return PORTRAIT
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putString("queryToSave",queryToSave)
        outState.putBoolean("FLAG",FLAG)

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        queryToSave= savedInstanceState.getString("queryToSave").toString()

        val request = ServiceBuilder.buildService(ApiInterface::class.java)

        getData(request,queryToSave)


    }

    fun getData(request : ApiInterface ,query : String){
        var dataFecher = GetGiphyAsyncTask(request, this, object : AsyncResponseGif {
            override fun processGifFinish(output: List<GiphySearchImagePoko.GiphySearchDatum>) {

                    dataList=output
                    if(!FLAG){

                        giphyAdapter = GifAdapter(
                            context = this@MainActivity,
                            data = output
                        )
                        recyclerView?.adapter=giphyAdapter
                        FLAG=true
                    }
                    else{
                        giphyAdapter?.setDataList(output)
                        giphyAdapter?.notifyDataSetChanged()
                    }
            }
        })

        dataFecher.execute(query)

    }


}




