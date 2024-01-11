package com.ymts0579.bookwormcycle

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.ymts0579.bookwormcycle.model.BooksResponse
import com.ymts0579.fooddonationapp.model.Userresponse
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class showbooks : AppCompatActivity() {
    lateinit var img1:ImageView
    lateinit var tvbid:TextView
    lateinit var tvbname:TextView
    lateinit var tvbauthor:TextView
    lateinit var tvbstatus:TextView
    lateinit var tvcdes:TextView
    lateinit var tvuserdea:TextView
    lateinit var linearuser:LinearLayout
    lateinit var tvname:TextView
    lateinit var tvemail:TextView
    lateinit var tvnum:TextView
    lateinit var tvcity:TextView
    lateinit var btnrequest:Button
 var email=""
    var  nwmail=""
    var path=""
var nameb=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showbooks)
        img1=findViewById(R.id.img1)
        tvbid=findViewById(R.id.tvbid)
        tvbname=findViewById(R.id.tvbname)
        tvbauthor=findViewById(R.id.tvbauthor)
        tvbstatus=findViewById(R.id.tvbstatus)
        tvcdes=findViewById(R.id.tvcdes)
        tvuserdea=findViewById(R.id.tvuserdea)
        linearuser=findViewById(R.id.linearuser)
        tvname=findViewById(R.id.tvname)
        tvemail=findViewById(R.id.tvemail)
        tvnum=findViewById(R.id.tvnum)
        tvcity=findViewById(R.id.tvcity)
        btnrequest=findViewById(R.id.btnrequest)
        linearuser.visibility= View.GONE
        val bookid=intent.getStringExtra("id").toString()


        getSharedPreferences("user", MODE_PRIVATE).apply {
            nwmail=getString("email","").toString()

        }

        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.usersbookselect(bookid,"userbookselected")
                .enqueue(object : Callback<BooksResponse> {
                    override fun onResponse(call: Call<BooksResponse>, response: Response<BooksResponse>) {
                        val kkk=response.body()!!.user[0]

                        tvbid.text=kkk.Bookid
                        tvbname.text=kkk.name
                        tvbauthor.text=kkk.author
                        tvbstatus.text=kkk.status
                        tvcdes.text=kkk.description
                         nameb=kkk.name
                        val uri= Uri.parse(kkk.path.trim())
                        path=kkk.path.trim()
                        Glide.with(this@showbooks).load(uri).into(img1)
                        email=kkk.email
                        tvuserdea.setOnClickListener {
                            linearuser.visibility=View.VISIBLE
                            readuser(email)
                        }


                    }

                    override fun onFailure(call: Call<BooksResponse>, t: Throwable) {
                        Toast.makeText(this@showbooks, "${t.message},${bookid}", Toast.LENGTH_SHORT).show()

                    }

                })
        }



        btnrequest.setOnClickListener {
            val calender = Calendar.getInstance()

            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH) + 1
            val day = calender.get(Calendar.DAY_OF_MONTH)
            val date = "$year-$month-$day"

            val name=nameb
            val author=tvbauthor.text.toString()
            val bookid=tvbid.text.toString()
            var oemail=email.toString()
            var nemail=nwmail
            var path=path
            val Status="Pending"


            if( tvbstatus.text.toString()=="Not Available"){
                Toast.makeText(this, "Book is not Available", Toast.LENGTH_SHORT).show()
            }
            else{
                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.instance.exchangebooks(name,author,bookid,oemail,nemail,path,Status,date,"Addexchange")
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

    private fun readuser(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.usersemail(email,"emailuser")
                .enqueue(object : Callback<Userresponse> {
                    override fun onResponse(call: Call<Userresponse>, response: Response<Userresponse>) {
                        val kkk=response.body()!!.user[0]

                        tvname.text=kkk.name
                        tvemail.text=kkk.email
                        tvnum.text=kkk.moblie
                        tvcity.text=kkk.city
                    }

                    override fun onFailure(call: Call<Userresponse>, t: Throwable) {
                        Toast.makeText(this@showbooks, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }

    }
}