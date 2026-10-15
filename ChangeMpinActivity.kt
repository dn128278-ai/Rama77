package com.rama77.app.ui.mpin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rama77.app.R

class ChangeMpinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Toast.makeText(this, "Set new 4-digit MPIN", Toast.LENGTH_SHORT).show()
    }
}