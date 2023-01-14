package com.example.projetbook

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_search = findViewById<Button>(R.id.button4);

        btn_search.setOnClickListener {
            val search_field = findViewById<TextInputEditText>(R.id.search_field);
            val text = search_field.text.toString();

            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            intent.putExtra("key", text)
            startActivity(intent)
        }
    }

    private fun searchForBook() {

    }
}

