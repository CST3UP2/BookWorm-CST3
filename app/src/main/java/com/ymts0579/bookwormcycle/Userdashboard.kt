package com.ymts0579.bookwormcycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Userdashboard : AppCompatActivity() {
    lateinit var fragment: Fragment
    lateinit var bottomNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userdashboard)
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottom()
        fragment=Userhome()
        callingFragment(fragment)
    }

    private fun bottom() {
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home ->{
                     fragment=Userhome()
                    callingFragment(fragment)
                    true
                }
                R.id.profile ->{
                    fragment=userprofile()
                    callingFragment(fragment)
                    true
                }
                R.id.hhistory->{
                    fragment=history()
                    callingFragment(fragment)
                    true
                }
                R.id.hlogout->{
                    logout()
                    true
                }
                else -> {
                    false
                }
            }
        }

    }

    private fun callingFragment(fragment: Fragment) {
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fcontainer, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun logout() {
        val alertdialog= AlertDialog.Builder(this)
        alertdialog.setIcon(R.drawable.ic_launcher_foreground)
        alertdialog.setTitle("LOGOUT")
        alertdialog.setIcon(R.drawable.logo)
        alertdialog.setCancelable(false)
        alertdialog.setMessage("Do you Want to Logout?")
        alertdialog.setPositiveButton("Yes"){ alertdialog, which->
            startActivity(Intent(this, Login::class.java))
            val  shared=getSharedPreferences("user", MODE_PRIVATE)
            shared.edit().clear().apply()
            finish()
            alertdialog.dismiss()
        }
        alertdialog.setNegativeButton("No"){alertdialog,which->
            Toast.makeText(this,"thank you", Toast.LENGTH_SHORT).show()
            alertdialog.dismiss()
        }
        alertdialog.show()

    }
}