package com.example.pick_a_flick

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.Image
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // retrieve username and put that in the greeting text
        val userNameSP: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userName = userNameSP.getString("text", "there") // default fallback name: "Hi there!"
        val username: TextView = findViewById(R.id.username)
        username.text = "$userName"

        // how to btn
        val howToBtn: ImageButton = findViewById(R.id.howToBtn)

        // genre dropdown and btn to request movies
        val genresDropdown: Spinner = findViewById(R.id.genresDropdown)
        val getMovieBtn: ImageButton = findViewById(R.id.getMovieBtn)

        // genres for dropdown
        val genres = arrayOf(
            "Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary",
            "Drama", "Family", "Fantasy", "History", "Horror", "Music", "Mystery",
            "Romance", "Science Fiction", "TV Movie", "Thriller", "War", "Western"
        )
        val genreMap = mapOf(
            "Action" to 28,
            "Adventure" to 12,
            "Animation" to 16,
            "Comedy" to 35,
            "Crime" to 80,
            "Documentary" to 99,
            "Drama" to 18,
            "Family" to 10751,
            "Fantasy" to 14,
            "History" to 36,
            "Horror" to 27,
            "Music" to 10402,
            "Mystery" to 9648,
            "Romance" to 10749,
            "Science Fiction" to 878,
            "TV Movie" to 10770,
            "Thriller" to 53,
            "War" to 10752,
            "Western" to 37
        )
        genresDropdown.adapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, genres) // link genremaps to spinner

        // movies list
        val movieListView: ListView = findViewById(R.id.movieListView)
        val movieTitles = mutableListOf<String>()
        val movieDetails = mutableListOf<JSONObject>()
        val adapter = ArrayAdapter(this, R.layout.list_item_yellow, movieTitles)
        movieListView.adapter = adapter

        // btn to go to watch list
        val watchListBtn: ImageButton = findViewById(R.id.watchListBtn)

        // request and display movies on btn click
        getMovieBtn.setOnClickListener {
            try {
                // store selected genre from dropdown
                val selectedGenre = genresDropdown.selectedItem.toString()
                val genreId = genreMap[selectedGenre]

                // values for api movie request
                val apiKey = "29cfa8b8983582f1717ebe926c19c721"
                val requestUrl = "https://api.themoviedb.org/3/discover/movie?api_key=$apiKey&with_genres=$genreId"

                val queue = Volley.newRequestQueue(this)

                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.GET, requestUrl, null,
                    { response ->
                        try {
                            val results = response.getJSONArray("results")
                            movieTitles.clear()
                            movieDetails.clear()

                            val allMovies = mutableListOf<JSONObject>()
                            for (i in 0 until results.length()) {
                                allMovies.add(results.getJSONObject(i))
                            }

                            allMovies.shuffle()

                            for (i in 0 until minOf(5, allMovies.size)) {
                                val movie = allMovies[i]
                                val title = movie.getString("title")
                                movieTitles.add("${i + 1}. $title")
                                movieDetails.add(movie)
                            }

                            adapter.notifyDataSetChanged()
                            /*movieListView.setOnItemClickListener { _, _, position, _ ->
                                val selectedMovie = movieDetails[position]
                                val title = selectedMovie.getString("title")
                                val releaseDate = selectedMovie.optString("release_date", "Unknown")
                                val overview = selectedMovie.optString("overview", "No overview available.")
                                val posterPath = selectedMovie.optString("poster_path", "")

                                val fullPosterUrl = "https://image.tmdb.org/t/p/w500$posterPath"

                                val info = """
                                Title: $title
                                Release Date: $releaseDate
                        
                                Overview: $overview
                                Poster: $fullPosterUrl
                            """.trimIndent()

                                AlertDialog.Builder(this)
                                    .setTitle("Movie Details")
                                    .setMessage(info)
                                    .setPositiveButton("OK", null)
                                    .show()
                            }*/

                            movieListView.setOnItemClickListener { _, _, position, _ ->
                                val selectedMovie = movieDetails[position]
                                val title = selectedMovie.getString("title")
                                val releaseDate = selectedMovie.optString("release_date", "Unknown")
                                val overview = selectedMovie.optString("overview", "No overview available.")
                                val posterPath = selectedMovie.optString("poster_path", "")

                                val intent = Intent(this, MovieInfo::class.java).apply {
                                    putExtra("title", title)
                                    putExtra("releaseDate", releaseDate)
                                    putExtra("overview", overview)
                                    putExtra("posterPath", posterPath)
                                }
                                startActivity(intent)
                            }


                        } catch (e: JSONException) {
                            Toast.makeText(this, "Error parsing data: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    },
                    { error ->
                        Toast.makeText(this, "Request failed: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                )

                queue.add(jsonObjectRequest) // api calling
            } catch (e: Exception) {
                Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()
            }
        }

        // go to watch list on btn click
        watchListBtn.setOnClickListener {
            val intent = Intent(this, WatchList::class.java)
            startActivity(intent)
        }

        // go to howTo on btn click
        howToBtn.setOnClickListener {
            val intent = Intent(this, Instructions::class.java)
            startActivity(intent)
        }
    }

    // func to display movieInfo popup
    /*private fun displayMovieInfo() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.activity_movie_info, null)

        val width = 1100
        val height = 2000

        val movieWindow = PopupWindow(popupView, width, height, true)
        movieWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        val closeButton = popupView.findViewById<ImageButton>(R.id.closeMovieInfo)

        // close window on btn click
        closeButton.setOnClickListener {
            movieWindow.dismiss()
        }
    }

    // func to display howTo popup
    private fun displayHowTo() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.activity_instructions, null)

        val width = 1100
        val height = 2000

        val howToWindow = PopupWindow(popupView, width, height, true)
        howToWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        val homeBtn = popupView.findViewById<ImageButton>(R.id.homeBtn)

        // close window on btn click
        homeBtn.setOnClickListener {
            howToWindow.dismiss()
        }
    }*/
}