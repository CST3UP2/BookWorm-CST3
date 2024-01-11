package com.ymts0579.bookwormcycle

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ymts0579.bookwormcycle.model.adbookresponse
import com.ymts0579.bookwormcycle.model.adbooks
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Viewbooks : AppCompatActivity() {
    lateinit var listbook:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewbooks)
        listbook=findViewById(R.id.listbook)
        listbook.layoutManager = LinearLayoutManager(this)
        listbook.setHasFixedSize(true)





        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.getadminbook()
                .enqueue(object : Callback<adbookresponse> {
                    override fun onResponse(call: Call<adbookresponse>, response: Response<adbookresponse>) {
                        listbook.adapter= booksviewadapter(
                            this@Viewbooks,
                            response.body()!!.user
                        )
                        Toast.makeText(this@Viewbooks, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<adbookresponse>, t: Throwable) {
                        Toast.makeText(this@Viewbooks, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }

    class booksviewadapter(var context: Context, var listdata: ArrayList<adbooks>):
        RecyclerView.Adapter<booksviewadapter.DataViewHolder>(){
        var id=0
        class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var img1=view.findViewById<ImageView>(R.id.img1)
            var tvbname=view.findViewById<TextView>(R.id.tvbname)
            var tvbauthor=view.findViewById<TextView>(R.id.tvbauthor)
            var tvbdes=view.findViewById<TextView>(R.id.tvbdes)
            var imaglink=view.findViewById<ImageView>(R.id.imaglink)


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.cardadbooks, parent, false)
            return DataViewHolder(view)
        }

        override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
            holder.apply {
                listdata.get(position).apply {
                    val uri= Uri.parse(path.trim())
                    Glide.with(context).load(uri).into(img1)
                    tvbname.text=name
                    tvbauthor.text="by\n"+author
                    tvbdes.text=description
                    imaglink.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        context.startActivity(intent)
                    }



                }
            }


        }



        override fun getItemCount() = listdata.size
    }
}