package com.example.projetbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import org.json.JSONObject
import org.json.JSONTokener

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
        recyclerView.layoutManager  = LinearLayoutManager(this)
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
            val imagelinks =
                jsonArray.getJSONObject(i).getJSONObject("volumeInfo").optJSONObject("imageLinks")
            val image = if (imagelinks == null) "" else imagelinks.getString("smallThumbnail")
            val description =
                jsonArray.getJSONObject(i).getJSONObject("volumeInfo").optString("description")
                    ?: ""

            books.add(
                BookType(
                    i.toLong(),
                    jsonArray.getJSONObject(i).getJSONObject("volumeInfo").getString("title"),
                    description,
                    image,
                    jsonArray.getJSONObject(i).getJSONObject("volumeInfo").getString("previewLink")
                )
            )
        }


        return books.toTypedArray()
    }
}