package com.example.projetbook

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import java.util.logging.Handler


class BooksAdapter(private val dataset: Array<BookType>, val listener: (BookType) -> Unit) :
    RecyclerView.Adapter<BooksAdapter.ViewHolder>() {
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(book: BookType, listener: (BookType) -> Unit) = with(itemView)
        {
            itemView.findViewById<TextView>(R.id.title).text = book.title;
            //itemView.findViewById<TextView>(R.id.description).text = book.description
            if (book.illustration.isEmpty()) {
                itemView.findViewById<ImageView>(R.id.illustration).setImageResource(R.mipmap.book_sample)
            } else {
                Picasso.get().load(book.illustration).into(itemView.findViewById<ImageView>(R.id.illustration))
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
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.line_book, viewGroup, false)
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataset[position], listener);
    }

    override fun getItemCount(): Int {
        return dataset.size;
    }
}