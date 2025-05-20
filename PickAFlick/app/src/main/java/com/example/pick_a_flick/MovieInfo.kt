package com.example.pick_a_flick

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class MovieInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val title = intent.getStringExtra("title")
        val releaseDate = intent.getStringExtra("releaseDate")
        val overview = intent.getStringExtra("overview")
        val posterPath = intent.getStringExtra("posterPath")

        val titleView: TextView = findViewById(R.id.movieTitle)
        val yearView: TextView = findViewById(R.id.movieYear)
        val overviewView: TextView = findViewById(R.id.movieOverview)
        val posterImageView: ImageView = findViewById(R.id.moviePoster)

        val addToWatchListBtn: ImageButton = findViewById(R.id.addToWatchListBtn)
        val closeButton: Button = findViewById(R.id.closeMovieInfo)

        titleView.text = title
        val year = releaseDate?.take(4) ?: "Unknown"
        yearView.text = "$year"
        overviewView.text = overview

        // Load image with Glide
        val imageUrl = "https://image.tmdb.org/t/p/w500$posterPath"
        Glide.with(this).load(imageUrl).into(posterImageView)

        // close window on btn click
        closeButton.setOnClickListener {
            finish()
        }

        // add to watch list on btn click
        addToWatchListBtn.setOnClickListener {
            val moviesSP: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = moviesSP.edit()

            val existingList = moviesSP.getStringSet("watchlist", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

            existingList.add(title ?: "Untitled Movie")
            editor.putStringSet("watchlist", existingList)
            editor.apply()

            Toast.makeText(this, "$title added to your watchlist!", Toast.LENGTH_SHORT).show()
        }
    }
}