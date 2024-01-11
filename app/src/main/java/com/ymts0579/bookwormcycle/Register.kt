package com.ymts0579.bookwormcycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {
    lateinit var etname: EditText
    lateinit var etunum: EditText
    lateinit var etemail: EditText
    lateinit var etuaddress: EditText
    lateinit var etucity: EditText
    lateinit var etupass: EditText
    lateinit var etupass1: EditText
    lateinit var btnsubmit: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        etname=findViewById(R.id.etname)
        etunum=findViewById(R.id.etunum)
        etemail=findViewById(R.id.etemail)
        etuaddress=findViewById(R.id.etuaddress)
        etucity=findViewById(R.id.etucity)
        etupass=findViewById(R.id.etupass)
        etupass1=findViewById(R.id.etupass1)
        btnsubmit=findViewById(R.id.btnsubmit)



        btnsubmit.setOnClickListener {
            val nam=etname.text.toString().trim()
            val num=etunum.text.toString().trim()
            val email=etemail.text.toString().trim()
            val add=etuaddress.text.toString().trim()
            val city=etucity.text.toString().trim()
            val pas=etupass.text.toString().trim()
            val pass=etupass1.text.toString().trim()
            if(nam.isNotEmpty()&&num.isNotEmpty()&&email.isNotEmpty()
                &&add.isNotEmpty()&&city.isNotEmpty()&&pas.isNotEmpty()&&pass.isNotEmpty()){
                if(num.count()==10 && pas==pass){
                    CoroutineScope(Dispatchers.IO).launch {
                        RetrofitClient.instance.register(nam,num,email,city,pass,add,"register")
                            .enqueue(object: Callback<DefaultResponse> {
                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                    t.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show() }
                                }
                                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                    response.body()?.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show()}
                                    etname.text!!.clear()
                                    etunum.text!!.clear()
                                    etemail.text!!.clear()
                                    etuaddress.text!!.clear()
                                    etucity.text!!.clear()
                                    etupass.text!!.clear()
                                    etupass1.text!!.clear()
                                }
                            })
                    }

                }else{
                    if(num.count()==10){
                        Snackbar.make(it,"password and conform password are not matched", Snackbar.LENGTH_SHORT).show()
                        etupass.setError("Enter your Password Properly")
                        etupass1.setError("Enter your Conform Password Properly")
                    }else{
                        Snackbar.make(it,"Enter Your phone number properly", Snackbar.LENGTH_SHORT).show()
                        etunum.setError("Enter your Number properly")
                    }
                }

            }else{
                Snackbar.make(it,"Enter the fields", Snackbar.LENGTH_SHORT).show()
                etname.setError("Enter your Name")
                etunum.setError("Enter your Number")
                etemail.setError("Enter your Email")
                etuaddress.setError("Enter your Address")
                etucity.setError("Enter your City")
                etupass.setError("Enter your Password ")
                etupass1.setError("Enter your Conform Password ")
            }
        }
    }
}