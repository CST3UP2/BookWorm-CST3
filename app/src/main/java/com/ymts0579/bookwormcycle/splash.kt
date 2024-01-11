package com.ymts0579.bookwormcycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val splashScreenTimeout=4000

        val email= getSharedPreferences("user", MODE_PRIVATE).getString("email","")
        if(email!!.isNotEmpty()){
            if(email=="admin"){
                startActivity(Intent(this,AdminDashboard::class.java))
                finish()
            }else{
                startActivity(Intent(this,Userdashboard::class.java))
                finish()

            }

        }else{
                val ii= Intent(this,Login::class.java)
                Handler().postDelayed({
                    startActivity(ii)
                    finish()
                },splashScreenTimeout.toLong())

        }

    }
}