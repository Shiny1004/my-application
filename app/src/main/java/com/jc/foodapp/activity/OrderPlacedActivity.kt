package com.jc.foodapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Button
import com.jc.foodapp.R

class OrderPlacedActivity : AppCompatActivity() {

    lateinit var btnok:Button

    lateinit var handler: Handler
    lateinit var runnable: Runnable//to close the next activity after few seconds if ok button is not clicked


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order_placed)

        btnok=findViewById(R.id.btnok)

        runnable= Runnable {
            finish()
        }
        handler= Handler()
        handler.postDelayed(runnable,3000)

        btnok.setOnClickListener{
            finish()
        }

    }
    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
    }

}

