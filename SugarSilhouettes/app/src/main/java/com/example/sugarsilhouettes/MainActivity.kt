package com.example.sugarsilhouettes

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    private lateinit var instructions: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // btns variables
        val colorsPrevBtn : ImageButton = findViewById(R.id.colorsPrevBtn)
        val colorsNextBtn : ImageButton = findViewById(R.id.colorsNextBtn)
        val shapesPrevBtn : ImageButton = findViewById(R.id.shapesPrevBtn)
        val shapesNextBtn : ImageButton = findViewById(R.id.shapesNextBtn)
        val makeBtn : ImageButton = findViewById(R.id.makeBtn)
        val againBtn : ImageButton = findViewById(R.id.againBtn)

        val resultCard : ImageView = findViewById(R.id.resultCard) // variable for result card
        var loadingGif : ImageView = findViewById(R.id.loadingGif)

        // color/shape containers variables
        val colorDisplay : ImageView = findViewById(R.id.colorDisplay)
        val shapeDisplay : ImageView = findViewById(R.id.shapeDisplay)

        // color options variables
        var currentColorIndex = 0 // to access colors from array using prev/next btns
        val colorOptions = arrayOf(R.drawable.color_white, R.drawable.color_brown, R.drawable.color_green, R.drawable.color_pink, R.drawable.color_blue)

        // shape options variables
        var currentShapeIndex = 0
        val shapeOptions = arrayOf(R.drawable.shape_square, R.drawable.shape_dome, R.drawable.shape_cylinder, R.drawable.shape_roll, R.drawable.shape_disc)

        // sfx variables
        val prevnextSFX = MediaPlayer.create(this, R.raw.sfx_prevnextbtn)
        val makeSFX = MediaPlayer.create(this, R.raw.sfx_makebtn)
        val resultSFX = MediaPlayer.create(this, R.raw.sfx_result)
        val instrestSFX = MediaPlayer.create(this, R.raw.sfx_instresbtn)

        // instructions popup
        instructions = findViewById(R.id.instBtn)
        instructions.setOnClickListener {
            instrestSFX.start() // play sfx
            displayInst()
        }

        // display initial color/shape
        colorDisplay.setImageResource(colorOptions[currentColorIndex])
        shapeDisplay.setImageResource(shapeOptions[currentShapeIndex])

        // func to display next color
        colorsNextBtn.setOnClickListener {
            if (currentColorIndex < colorOptions.size - 1) {
                prevnextSFX.start() // play sfx
                currentColorIndex += 1
                colorDisplay.setImageResource(colorOptions[currentColorIndex])
            }
        }

        // func to display previous color
        colorsPrevBtn.setOnClickListener {
            if (currentColorIndex > 0) {
                prevnextSFX.start() // play sfx
                currentColorIndex -= 1
                colorDisplay.setImageResource(colorOptions[currentColorIndex])
            }
        }

        // func to display next shape
        shapesNextBtn.setOnClickListener {
            if (currentShapeIndex < shapeOptions.size - 1) {
                prevnextSFX.start() // play sfx
                currentShapeIndex += 1
                shapeDisplay.setImageResource(shapeOptions[currentShapeIndex])
            }
        }

        // func to display previous shape
        shapesPrevBtn.setOnClickListener {
            if (currentShapeIndex > 0) {
                prevnextSFX.start() // play sfx
                currentShapeIndex -= 1
                shapeDisplay.setImageResource(shapeOptions[currentShapeIndex])
            }
        }

        // initially hide result card & restart btn
        loadingGif.setVisibility(View.GONE)
        resultCard.setVisibility(View.GONE)
        againBtn.setVisibility(View.GONE)

        // func to display result
        makeBtn.setOnClickListener {
            makeSFX.start() // play sfx

            // add the gif using glide library then display it
            loadingGif.setVisibility(View.VISIBLE)

            // make btns behind the card unclickable
            colorsPrevBtn.setEnabled(false)
            colorsNextBtn.setEnabled(false)
            shapesPrevBtn.setEnabled(false)
            shapesNextBtn.setEnabled(false)

            // display result according to the selected color (currentColorIndex) and shape (currentShapeIndex)
            when (currentColorIndex) {
                //white
                0 -> {
                    Glide.with(this).load(R.drawable.loading_white).into(loadingGif) //white loading gif
                    when (currentShapeIndex) {
                        0 -> resultCard.setImageResource(R.drawable.white_square)
                        1 -> resultCard.setImageResource(R.drawable.white_dome)
                        2 -> resultCard.setImageResource(R.drawable.white_cylinder)
                        3 -> resultCard.setImageResource(R.drawable.white_roll)
                        4 -> resultCard.setImageResource(R.drawable.white_disc)
                    }
                }

                //brown
                1 -> {
                    Glide.with(this).load(R.drawable.loading_brown).into(loadingGif) //brown loading gif
                    when (currentShapeIndex) {
                        0 -> resultCard.setImageResource(R.drawable.brown_square)
                        1 -> resultCard.setImageResource(R.drawable.brown_dome)
                        2 -> resultCard.setImageResource(R.drawable.brown_cylinder)
                        3 -> resultCard.setImageResource(R.drawable.brown_roll)
                        4 -> resultCard.setImageResource(R.drawable.brown_disc)
                    }
                }

                //green
                2 -> {
                    Glide.with(this).load(R.drawable.loading_green).into(loadingGif) //green loading gif
                    when (currentShapeIndex) {
                        0 -> resultCard.setImageResource(R.drawable.green_square)
                        1 -> resultCard.setImageResource(R.drawable.green_dome)
                        2 -> resultCard.setImageResource(R.drawable.green_cylinder)
                        3 -> resultCard.setImageResource(R.drawable.green_roll)
                        4 -> resultCard.setImageResource(R.drawable.green_disc)
                    }
                }

                //pink
                3 -> {
                    Glide.with(this).load(R.drawable.loading_pink).into(loadingGif) //pink loading gif
                    when (currentShapeIndex) {
                        0 -> resultCard.setImageResource(R.drawable.pink_square)
                        1 -> resultCard.setImageResource(R.drawable.pink_dome)
                        2 -> resultCard.setImageResource(R.drawable.pink_cylinder)
                        3 -> resultCard.setImageResource(R.drawable.pink_roll)
                        4 -> resultCard.setImageResource(R.drawable.pink_disc)
                    }
                }

                //blue
                4 -> {
                    Glide.with(this).load(R.drawable.loading_blue).into(loadingGif) //blue loading gif
                    when (currentShapeIndex) {
                        0 -> resultCard.setImageResource(R.drawable.blue_square)
                        1 -> resultCard.setImageResource(R.drawable.blue_dome)
                        2 -> resultCard.setImageResource(R.drawable.blue_cylinder)
                        3 -> resultCard.setImageResource(R.drawable.blue_roll)
                        4 -> resultCard.setImageResource(R.drawable.blue_disc)
                    }
                }
            }

            // display result card after gif is displayed for 3s
            Handler(Looper.getMainLooper()).postDelayed({
                resultSFX.start() // play sfx

                loadingGif.setVisibility(View.GONE)
                resultCard.setVisibility(View.VISIBLE)
                againBtn.setVisibility(View.VISIBLE)
            }, 3000)
        }

        // func to restart
        againBtn.setOnClickListener {
            instrestSFX.start() // play sfx

            // hide result card & restart btn again
            resultCard.setVisibility(View.GONE)
            againBtn.setVisibility(View.GONE)

            // return color & shape display to default
            currentColorIndex = 0
            currentShapeIndex = 0
            colorDisplay.setImageResource(colorOptions[currentColorIndex])
            shapeDisplay.setImageResource(shapeOptions[currentShapeIndex])

            // make btns behind the card clickable
            colorsPrevBtn.setEnabled(true)
            colorsNextBtn.setEnabled(true)
            shapesPrevBtn.setEnabled(true)
            shapesNextBtn.setEnabled(true)
        }
    }

    // func to display popup instructions
    private fun displayInst() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.activity_instructions, null)

        val width = 1100
        val height = 2000

        val instructWindow = PopupWindow(popupView, width, height, true)
        instructWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        val closeButton = popupView.findViewById<ImageButton>(R.id.closeInstBtn)
        val instrestSFX = MediaPlayer.create(this, R.raw.sfx_instresbtn)

        closeButton.setOnClickListener {
            instrestSFX.start()
            instructWindow.dismiss()
        }
    }
}