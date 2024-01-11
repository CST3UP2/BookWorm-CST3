package com.ymts0579.bookwormcycle

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ymts0579.bookwormcycle.model.Books
import com.ymts0579.bookwormcycle.model.BooksResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchBooks : AppCompatActivity() {
    private lateinit var fused:FusedLocationProviderClient
    lateinit var listuserbook: RecyclerView
    lateinit var etbookname:EditText
    lateinit var btnsearch:ImageView
    lateinit var listuserbook1:RecyclerView
    var email=""
    var geo=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_books)
        fused=LocationServices.getFusedLocationProviderClient(this)
        listuserbook=findViewById(R.id.listuserbook)
        listuserbook1=findViewById(R.id.listuserbook1)
        etbookname=findViewById(R.id.etbookname)
        btnsearch=findViewById(R.id.btnsearch)
        listuserbook.layoutManager = LinearLayoutManager(this)
        listuserbook.setHasFixedSize(true)

        listuserbook1.layoutManager = LinearLayoutManager(this)
        listuserbook1.setHasFixedSize(true)

        getSharedPreferences("user", MODE_PRIVATE).apply {
            email=getString("email","").toString()

        }



        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION),10)


        }else{
            fused.lastLocation.addOnSuccessListener{
                 geo=Geocoder(this).getFromLocation(it.latitude,it.longitude,1).get(0).locality.toString().trim()
                Toast.makeText(this, "$geo", Toast.LENGTH_SHORT).show()
                showbooks()
            }
            fused.lastLocation.addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
            fused.lastLocation.addOnCanceledListener {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }







        btnsearch.setOnClickListener {

            val name=etbookname.text.toString()
            if(name.isEmpty()){
                etbookname.setError("Enter your needed Book name")

            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.instance.searchbookname("$name",geo,"search")
                        .enqueue(object : Callback<BooksResponse> {
                            override fun onResponse(call: Call<BooksResponse>, response: Response<BooksResponse>) {

                                listuserbook.adapter=searchbooksviewadapter(this@SearchBooks,response.body()!!.user,email)
                                listuserbook1.visibility=View.GONE
                                //Toast.makeText(this@SearchBooks, "${response.body()!!.user}", Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(call: Call<BooksResponse>, t: Throwable) {
                                Toast.makeText(this@SearchBooks, "${t.message}", Toast.LENGTH_SHORT).show()

                            }

                        })
                }
            }
        }
    }

    private fun showbooks() {

        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.searchbooks(geo,"searchbooks")
                .enqueue(object : Callback<BooksResponse> {
                    override fun onResponse(call: Call<BooksResponse>, response: Response<BooksResponse>) {

                        listuserbook1.adapter=searchbooksviewadapter(this@SearchBooks,response.body()!!.user,email)

                        Toast.makeText(this@SearchBooks, "${response.body()!!.user}", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<BooksResponse>, t: Throwable) {
                        Toast.makeText(this@SearchBooks, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }



}


class searchbooksviewadapter(var context: Context, var listdata: ArrayList<Books>,var email:String):
    RecyclerView.Adapter<searchbooksviewadapter.DataViewHolder>(){
    var id=0
    class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var img1=view.findViewById<ImageView>(R.id.img1)
        var tvbid=view.findViewById<TextView>(R.id.tvbid)
        var tvbname=view.findViewById<TextView>(R.id.tvbname)
        var tvbauthor=view.findViewById<TextView>(R.id.tvbauthor)
        var tvbstatus=view.findViewById<TextView>(R.id.tvbstatus)
        var tvcdes=view.findViewById<TextView>(R.id.tvcdes)
        var linearbooks=view.findViewById<LinearLayout>(R.id.linearbooks)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.searchbooks, parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
        val kk = listdata.get(position)
        if(email==kk.email){
            holder.linearbooks.visibility=View.GONE
        }

        holder.tvbid.text=kk.Bookid
        holder.tvbname.text=kk.name
        holder.tvbauthor.text=kk.author
        holder.tvbstatus.text=kk.status
        holder.tvcdes.text=kk.description

        val uri= Uri.parse(kk.path.trim())
        Glide.with(context).load(uri).into(holder.img1)


        holder.itemView.setOnClickListener {
            var intent= Intent(context,showbooks::class.java)
            intent.putExtra("id",kk.Bookid)
            context.startActivity(intent)
        }

    }

    override fun getItemCount() = listdata.size
}




