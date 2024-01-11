package com.ymts0579.bookwormcycle

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
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

class AdminAddbooks : AppCompatActivity() {
    lateinit var etbname:EditText
    lateinit var etbauthor:EditText
    lateinit var etbsec:EditText
    lateinit var etblink:EditText
    lateinit var imgadd:ImageView
    lateinit var btnadd:Button
    var encode=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_addbooks)
        etbname = findViewById(R.id.etbname)
        etbauthor = findViewById(R.id.etbauthor)
        etbsec = findViewById(R.id.etbsec)
        etblink = findViewById(R.id.etblink)
        imgadd = findViewById(R.id.imgadd)
        btnadd = findViewById(R.id.btnadd)

        imgadd.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 90)
        }

        btnadd.setOnClickListener {
            val name = etbname.text.toString().trim()
            val desc = etbsec.text.toString().trim()
            val author = etbauthor.text.toString().trim()
            val link = etblink.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Enter Book name", Toast.LENGTH_SHORT).show()
            } else if (desc.isEmpty()) {
                Toast.makeText(this, "Enter Book description", Toast.LENGTH_SHORT).show()
            } else if (author.isEmpty()) {
                Toast.makeText(this, "Enter Book Author", Toast.LENGTH_SHORT).show()
            } else if (link.isEmpty()) {
                Toast.makeText(this, "Enter Book Link", Toast.LENGTH_SHORT).show()
            } else if (encode == "") {
                Toast.makeText(this, "Add Book images", Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.instance.Addadbooks(name, author, desc, encode, link, "Addbooks")
                        .enqueue(object : Callback<DefaultResponse> {
                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                t.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show()}
                            }

                            override fun onResponse(
                                call: Call<DefaultResponse>,
                                response: Response<DefaultResponse>
                            ) {
                                response.body()?.message?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show()}
                                startActivity(Intent(this@AdminAddbooks,Adminviewbooks::class.java))
                                finish()

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
            encode = Base64.encodeToString(image, Base64.DEFAULT)


        }
    }
}