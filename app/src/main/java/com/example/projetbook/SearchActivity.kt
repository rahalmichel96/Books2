package com.example.projetbook

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header


class SearchActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    lateinit var books: ArrayList<BookType>
    private var bookAdapter: BooksAdapter? = null


    private var nestedSV: NestedScrollView? = null

    private var loadingPB: ProgressBar? = null

    // creating a variable for our page and limit as 2
    // as our api is having highest limit as 2 so
    // we are setting a limit = 2
    private var page: Int = 0
    private var limit: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.results_search)
        //bouchon
        nestedSV = findViewById(R.id.idNestedSV)

        loadingPB = findViewById(R.id.idPBLoading);
        books = ArrayList()

        //val booksAdapter : BooksAdapter = BooksAdapter(books, { book -> bookOnclick() })

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView!!.layoutManager  = LinearLayoutManager(this@SearchActivity)
        recyclerView!!.adapter = BooksAdapter(books.toTypedArray(), {book -> bookOnclick(book)}, this@SearchActivity)

        getResults(page, limit)

        // adding on scroll change listener method for our nested scroll view.
        // adding on scroll change listener method for our nested scroll view.
        nestedSV?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                // in this method we are incrementing page number,
                // making progress bar visible and calling get data method.
                page++
                loadingPB?.visibility = View.VISIBLE
                getResults(page, limit)
            }
        })
    }

    private fun bookOnclick(book : BookType) {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(book.link))
        startActivity(browserIntent)
    }

    fun getResults(page : Int, limit : Int) {

        if (page > limit) {
            Log.i("FOUND : ", "ON Y EST")
            // checking if the page number is greater than limit.
            // displaying toast message in this case when page>limit.
            Toast.makeText(this, "C'est tout pour le moment...", Toast.LENGTH_SHORT).show();

            // hiding our progress bar.
            loadingPB?.visibility = View.GONE;
            return;
        }

        // creating a new variable for our request queue
        val queue = Volley.newRequestQueue(this@SearchActivity)

        val bundle : Bundle? = intent.extras
        val text: String? = intent.getStringExtra("key")

        //val client : AsyncHttpClient = AsyncHttpClient();
        val jsonRequest = JsonObjectRequest(Request.Method.GET,"https://www.googleapis.com/books/v1/volumes?q=$text&startIndex=$page", null,
            { jsonObject ->
                try {
                    Log.i("FOUND : ", jsonObject.toString())

                    val jsonArray = jsonObject.getJSONArray("items")

                    for (i in 0 until jsonArray.length()) {
                        val imagelinks =
                            jsonArray.getJSONObject(i).getJSONObject("volumeInfo").optJSONObject("imageLinks")
                        val image = if (imagelinks == null) "" else imagelinks.getString("smallThumbnail");
                        /*val description =
                                        jsonArray.getJSONObject(i).getJSONObject("volumeInfo").optString("description")
                                            ?: ""*/

                        books.add(
                            BookType(
                                i.toLong(),
                                jsonArray.getJSONObject(i).getJSONObject("volumeInfo").getString("title"),
                                image,
                                jsonArray.getJSONObject(i).getJSONObject("volumeInfo").getString("previewLink")
                            )
                        )
                    }


                    bookAdapter = BooksAdapter(books.toTypedArray(), { book -> bookOnclick(book) }, this@SearchActivity)
                    recyclerView!!.layoutManager  = LinearLayoutManager(this@SearchActivity)
                    recyclerView!!.adapter = bookAdapter
                    bookAdapter!!.notifyDataSetChanged()
                    loadingPB?.visibility = View.INVISIBLE


                } catch (e : Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                        Log.e("TAG", "RESPONSE IS $error")
                        // handling on error listener method.
                        Toast.makeText(
                            this@SearchActivity,
                            "Impossible de récupérer les résultats..",
                            Toast.LENGTH_SHORT
                        ).show();
            }
        )
        /*val request = client.get("https://www.googleapis.com/books/v1/volumes?q=$text",
            object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONObject?
            ) {


            }

        })*/


        queue.add(jsonRequest);
    }
}