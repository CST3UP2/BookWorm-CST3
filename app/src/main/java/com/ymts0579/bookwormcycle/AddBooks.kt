package com.ymts0579.bookwormcycle

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class AddBooks : AppCompatActivity() {
    lateinit var imgadd:ImageView
    lateinit var etbname:EditText
    lateinit var  etbauthor:EditText
    lateinit var etbsec:EditText
    lateinit var tvid:TextView
    lateinit var btnadd:Button
    lateinit var  btnshow:Button
    var encode=""
    var email=""
    var city=""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_books)
        imgadd=findViewById(R.id.imgadd)
        etbname=findViewById(R.id.etbname)
        etbauthor=findViewById(R.id.etbauthor)
        etbsec=findViewById(R.id.etbsec)
        tvid=findViewById(R.id.tvid)
        btnadd=findViewById(R.id.btnadd)
        btnshow=findViewById(R.id.btnshow)
        tvid.setText("Bookid${(1000..9999).shuffled().last()}")


        getSharedPreferences("user", MODE_PRIVATE).apply {
           email= getString("email","").toString()
           city= getString("city","").toString()
        }


        btnshow.setOnClickListener { startActivity(Intent(this,ViewuserBooks::class.java)) }


        imgadd.setOnClickListener{
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,90)
        }


        btnadd.setOnClickListener {
            val name=etbname.text.toString().trim()
            val author=etbauthor.text.toString().trim()
            val desc=etbsec.text.toString().trim()
            val id=tvid.text.toString().trim()
            if(name.isEmpty()&&author.isEmpty()&&desc.isEmpty()&&encode==""){
                etbname.setError("Enter your Book Name")
                etbauthor.setError("Enter your Book")
                etbsec .setError("Enter your Book")
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.instance.Addbooks(name,author,desc,"$email","Available","$city",encode,"$id","Addbooks")
                        .enqueue(object: Callback<DefaultResponse> {
                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                t.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show() }
                            }
                            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                                response.body()?.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show()}

                            }
                        })
                }

            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 90 && resultCode == RESULT_OK) {
            val tt1 = data!!.extras!!.get("data")as Bitmap
            Glide.with(this).load(tt1).into(imgadd)
            val byte = ByteArrayOutputStream()
            tt1.compress(Bitmap.CompressFormat.JPEG, 100, byte)
            val image = byte.toByteArray()
            encode =Base64.encodeToString(image, Base64.DEFAULT)


        }
    }
}