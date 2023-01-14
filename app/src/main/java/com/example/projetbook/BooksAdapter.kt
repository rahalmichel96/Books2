package com.example.projetbook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.Executors


class BooksAdapter(private val dataset: Array<BookType>, val listener: (BookType) -> Unit, val context: Context) :
    RecyclerView.Adapter<BooksAdapter.ViewHolder>() {



    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(book: BookType, listener: (BookType) -> Unit) = with(itemView)
        {
            itemView.findViewById<TextView>(R.id.title).text = book.title;
            //itemView.findViewById<TextView>(R.id.description).text = book.description
            if (book.illustration.isEmpty()) {
                itemView.findViewById<ImageView>(R.id.illustration).setImageResource(R.mipmap.book_sample_foreground)
            } else {
                // Declaring and initializing the ImageView
                val imageView = findViewById<ImageView>(R.id.illustration)

                // Declaring executor to parse the URL
                val executor = Executors.newSingleThreadExecutor()

                // Once the executor parses the URL
                // and receives the image, handler will load it
                // in the ImageView
                val handler = android.os.Handler(Looper.getMainLooper())

                // Initializing the image
                var image: Bitmap? = null

                // Only for Background process (can take time depending on the Internet speed)
                executor.execute {

                    // Image URL
                    val imageURL = book.illustration

                    // Tries to get the image and post it in the ImageView
                    // with the help of Handler
                    try {
                        val `in` = java.net.URL(imageURL).openStream()
                        image = BitmapFactory.decodeStream(`in`)

                        // Only for making changes in UI
                        handler.post {
                            imageView.setImageBitmap(image)
                        }
                    }

                    // If the URL doesnot point to
                    // image or any other kind of failure
                    catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            /*itemView.findViewById<Button>(R.id.linkToBook).setOnClickListener {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(book.link))
                context.startActivity(browserIntent)
            }*/
            setOnClickListener { listener(book) }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.line_book, viewGroup, false)
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book : BookType = dataset[position]
        holder.bind(book, listener);
    }

    override fun getItemCount(): Int {
        return dataset.size;
    }
}