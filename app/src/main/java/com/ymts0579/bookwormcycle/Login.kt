package com.ymts0579.bookwormcycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.ymts0579.model.model.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    lateinit var etemail:EditText
    lateinit var etpaswd:EditText
    lateinit var btnlgin:Button
    lateinit var tvsignup:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etemail=findViewById(R.id.etemail)
        etpaswd=findViewById(R.id.etpaswd)
        btnlgin=findViewById(R.id.btnlgin)
        tvsignup=findViewById(R.id.tvsignup)


        tvsignup.setOnClickListener{startActivity(Intent(this,Register::class.java))}



        btnlgin.setOnClickListener {
            val email=etemail.text.toString().trim()
            val pass=etpaswd.text.toString().trim()


            if(email.isEmpty()){
                Snackbar.make(it,"Enter your Email", Snackbar.LENGTH_SHORT).show()
                etemail.setError("Enter your Email")

            }else if(pass.isEmpty()){
                etpaswd.setError("Enter your Password")
                Snackbar.make(it,"Enter your Password", Snackbar.LENGTH_SHORT).show()
            }else if(email.contains("admin")&& pass.contains("admin")){
                startActivity(Intent(this,AdminDashboard::class.java))
                getSharedPreferences("user", MODE_PRIVATE).edit().putString("email","admin").apply()
                finish()
            } else{

                    CoroutineScope(Dispatchers.IO).launch {
                        RetrofitClient.instance.login(email,pass,"login")
                            .enqueue(object: Callback<LoginResponse> {
                                override fun onResponse(
                                    call: Call<LoginResponse>, response: Response<LoginResponse>
                                ) {
                                    if(!response.body()?.error!!){
                                        val type=response.body()?.user
                                        if (type!=null) {
                                            getSharedPreferences("user", MODE_PRIVATE).edit().apply {
                                                putString("mob",type.moblie)
                                                putString("pass",type.password)
                                                putString("email",type.email)
                                                putString("name",type.name)
                                                putString("address",type.address)
                                                putString("city",type.city)
                                                putInt("id",type.id)

                                                apply()
                                            }
                                            startActivity(Intent(this@Login,Userdashboard::class.java))
                                            finish()

                                        }
                                    }else{
                                        Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                                    t.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show() }

                                }

                            })

                    }

            }

        }
    }
}