package com.example.imagenfsw

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color.green
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.provider.Settings.Global
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.imagenfsw.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.nipunru.nsfwdetector.NSFWDetector
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)

        val imageUrl = " https://cdni.pornpics.com/460/1/310/59604542/59604542_011_2aac.jpg"
        GlobalScope.launch {
            val bitmap = loadBitmapFromUrl(imageUrl)
            Log.e("TAG", "onCreate: -----> " + bitmap?.height)
                runOnUiThread {
                    binding.immage.setImageBitmap(bitmap)
                }
            runDetection(bitmap)
        }

    }

    private fun runDetection(bitmap: Bitmap?) {
        val demobitmap = bitmap
        val confidenceThreshold: Float = 0.7.toFloat()//(Optional) Default is 0.7
        demobitmap?.let {
            NSFWDetector.isNSFW(demobitmap, confidenceThreshold) { isNSFW, confidence, image ->

                if (isNSFW) {
                   runOnUiThread {
                       binding.text.text = isNSFW.toString()
                       binding.text.setBackgroundColor(resources.getColor(R.color.dangers))
                   }
                    Log.e("TAG", "runDetection: " + isNSFW)
                    Log.e("TAG", "runDetection: $confidence")
                    Toast.makeText(this, "NSFW with confidence: $confidence", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    runOnUiThread {
                        binding.text.text = isNSFW.toString()
                        binding.text.setBackgroundColor(resources.getColor(R.color.green))
                    }

                    Log.e("TAG", "runDetection: $confidence")
                    Toast.makeText(this, "SFW with confidence: $confidence", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }



}