package com.example.projetbook

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL

class SearchActivity : AppCompatActivity() {
    lateinit var monRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.results_search)
        //bouchon
        val bundle : Bundle? = intent.extras
        val json: String? = intent.getStringExtra("results")

        val books = getResults(json)

        val booksAdapter : BooksAdapter = BooksAdapter(books, { book -> bookOnclick() })

        val recyclerView : RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = booksAdapter

    }

    private fun bookOnclick() {
        TODO("Not yet implemented")
    }

    fun getResults(results : String?) : Array<BookType> {
        val jsonObject = JSONTokener(results).nextValue() as JSONObject

        val jsonArray = jsonObject.getJSONArray("items")

        val books = ArrayList<BookType>()

        for (i in 0 until jsonArray.length()) {
            books.add(BookType(
                i.toLong(),
                jsonArray.getJSONObject(i).getJSONObject("volumeInfo").getString("title"),
                jsonArray.getJSONObject(i).getJSONObject("volumeInfo").getString("description"),
                jsonArray.getJSONObject(i).getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("smallThumbnail"),
                jsonArray.getJSONObject(i).getJSONObject("volumeInfo").getString("selflink")))
        }


        return books.toTypedArray()
    }
}