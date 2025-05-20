package com.example.pick_a_flick

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WatchList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_watch_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // retrieve username and put that in the greeting text
        val userNameSP: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userName = userNameSP.getString("text", "User") // default fallback
        val username: TextView = findViewById(R.id.username)
        username.text = "$userName's"

        val homeBtn: ImageButton = findViewById(R.id.homeBtn)

        val watchListView: ListView = findViewById(R.id.movieListView)
        val moviesSP: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val watchlistSet = moviesSP.getStringSet("watchlist", setOf()) ?: setOf()

        val watchlist = watchlistSet.toMutableList() // Use mutable list for modification
        val adapter = ArrayAdapter(this, R.layout.list_item_pink, watchlist)
        watchListView.adapter = adapter

        watchListView.setOnItemLongClickListener { _, _, position, _ ->
            val movieToRemove = watchlist[position]

            val dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
            val messageView = dialogView.findViewById<TextView>(R.id.alertMessage)
            messageView.text = "Are you sure you want to remove \"$movieToRemove\"?"

            val alertDialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton("Yes", null) // we'll override this later
                .setNegativeButton("Cancel", null)
                .create()

            alertDialog.setOnShowListener {
                // Match button area background to dialog background
                alertDialog.window?.decorView?.let { dialogView ->
                    val parent = dialogView.findViewById<ViewGroup>(android.R.id.content)
                    parent?.getChildAt(0)?.setBackgroundColor(Color.parseColor("#373737"))
                }

                val yesBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                val cancelBtn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

                // Set text color
                yesBtn.setTextColor(Color.parseColor("#93C3F6"))
                cancelBtn.setTextColor(Color.parseColor("#93C3F6"))

                // Set font
                val customFont = ResourcesCompat.getFont(this, R.font.neueremansans)
                yesBtn.typeface = customFont
                cancelBtn.typeface = customFont
                yesBtn.textSize = 18f
                cancelBtn.textSize = 18f

                // Click listeners
                yesBtn.setOnClickListener {
                    watchlist.removeAt(position)
                    adapter.notifyDataSetChanged()

                    moviesSP.edit()
                        .putStringSet("watchlist", watchlist.toSet())
                        .apply()

                    Toast.makeText(this, "\"$movieToRemove\" removed!", Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                }

                cancelBtn.setOnClickListener {
                    alertDialog.dismiss()
                }
            }

            alertDialog.show()
            true
        }

        // close window on btn click
        homeBtn.setOnClickListener {
            finish()
        }
    }
}