package com.example.imagenfsw

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.imagenfsw.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.nipunru.nsfwdetector.NSFWDetector
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var arralist = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)

        val myadapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arralist)
        binding.text.adapter =myadapter

        val imageUrl = "https://cdni.pornpics.de/460/7/107/57382708/57382708_003_1e2e.jpg"

////    val imageUrl = "http://i1.ekogate.club/images/redpornpictures.com/2/943_Amber_Tn.jpg"


        GlobalScope.launch {
            val bitmap = loadBitmapFromUrl(imageUrl)
            Log.e("TAG", "onCreate: -----> " + bitmap?.height)
                runOnUiThread {
                    binding.immage.setImageBitmap(bitmap)
                }
            runDetection(bitmap,myadapter)
        }

    }

    private fun runDetection(bitmap: Bitmap?, myadapter: ArrayAdapter<String>) {
        val demobitmap = bitmap
        val confidenceThreshold: Float = 0.1.toFloat()//(Optional) Default is 0.7
        demobitmap?.let {
            NSFWDetector.isNSFW(demobitmap, confidenceThreshold) { isNSFW, confidence, image ->

                if (isNSFW) {
                   runOnUiThread {
                       arralist.add(confidence.toString())
                       myadapter.notifyDataSetChanged()
                       binding.text.setBackgroundColor(resources.getColor(R.color.dangers))
                   }
                    Log.e("TAG", "runDetection: " + isNSFW)
                    Log.e("TAG", "runDetection: $confidence")
                    Toast.makeText(this, "NSFW with confidence: $confidence", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    runOnUiThread {
                        arralist.add(confidence.toString())
                        myadapter.notifyDataSetChanged()
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