package com.ymts0579.bookwormcycle

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.gsm.SmsManager
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
import com.ymts0579.bookwormcycle.model.Books
import com.ymts0579.bookwormcycle.model.BooksResponse
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewuserBooks : AppCompatActivity() {
    lateinit var listuserbook:RecyclerView
    var email=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewuser_books)
        listuserbook=findViewById(R.id.listuserbook)
        listuserbook.layoutManager = LinearLayoutManager(this)
        listuserbook.setHasFixedSize(true)

        getSharedPreferences("user", MODE_PRIVATE).apply {
            email=getString("email","").toString()

        }


        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.usersbooks("$email","userbooks")
                .enqueue(object : Callback<BooksResponse> {
                    override fun onResponse(call: Call<BooksResponse>, response: Response<BooksResponse>) {

                       listuserbook.adapter=userbooksviewadapter(this@ViewuserBooks,response.body()!!.user)


                    }

                    override fun onFailure(call: Call<BooksResponse>, t: Throwable) {
                        Toast.makeText(this@ViewuserBooks, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
    }



    class userbooksviewadapter(var context: Context, var listdata: ArrayList<Books>):
        RecyclerView.Adapter<userbooksviewadapter.DataViewHolder>(){
        var id=0
        class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var img1=view.findViewById<ImageView>(R.id.img1)
            var tvbid=view.findViewById<TextView>(R.id.tvbid)
            var tvbname=view.findViewById<TextView>(R.id.tvbname)
            var tvbauthor=view.findViewById<TextView>(R.id.tvbauthor)
            var tvbstatus=view.findViewById<TextView>(R.id.tvbstatus)
            var tvcdes=view.findViewById<TextView>(R.id.tvcdes)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.userbooks, parent, false)
            return DataViewHolder(view)
        }

        override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
            var kk = listdata.get(position)
            holder.tvbid.text=kk.Bookid
            holder.tvbname.text=kk.name
            holder.tvbauthor.text=kk.author
            holder.tvbstatus.text=kk.status
            holder.tvcdes.text=kk.description

            val uri= Uri.parse(kk.path.trim())
            Glide.with(context).load(uri).into(holder.img1)

            holder.itemView.setOnClickListener {
                id=listdata[position].id
                var alertdialog= AlertDialog.Builder(context)
                alertdialog.setIcon(R.drawable.ic_launcher_foreground)
                alertdialog.setTitle("Remove Books")
                alertdialog.setIcon(R.drawable.logo)
                alertdialog.setCancelable(false)
                alertdialog.setMessage("Are you want remove book?")
                alertdialog.setPositiveButton("yes"){ alertdialog, which->
                    deletebook(id)
                    alertdialog.dismiss()
                }
                alertdialog.setNegativeButton("No"){alertdialog,which->

                    alertdialog.dismiss()
                }


                alertdialog.show()
            }

        }

        private fun deletebook(id: Int) {
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.deletebooks(id,"deletbook")
                    .enqueue(object: Callback<DefaultResponse> {
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            Toast.makeText(context, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()


                        }
                    })
            }

        }

        override fun getItemCount() = listdata.size
    }
}



