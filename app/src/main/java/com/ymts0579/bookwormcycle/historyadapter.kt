package com.ymts0579.bookwormcycle

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ymts0579.bookwormcycle.model.Books
import com.ymts0579.bookwormcycle.model.exchange
import com.ymts0579.fooddonationapp.model.Userresponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class historyadapter(var context: Context, var listdata: ArrayList<exchange>):
    RecyclerView.Adapter<historyadapter.DataViewHolder>(){
    var id=0
    class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var img1=view.findViewById<ImageView>(R.id.img1)
       var tvbid=view.findViewById<TextView>(R.id.tvbid)
       var tvbname=view.findViewById<TextView>(R.id.tvbname)
       var tvbauthor=view.findViewById<TextView>(R.id.tvbauthor)
       var tvbstatus=view.findViewById<TextView>(R.id.tvbstatus)
       var tvcdes=view.findViewById<TextView>(R.id.tvcdes)
        var tvmoredetails=view.findViewById<TextView>(R.id.tvmoredetails)
        var linearuser=view.findViewById<LinearLayout>(R.id.linearuser)
        var  tvname=view.findViewById<TextView>(R.id.tvname)
        var tvnum=view.findViewById<TextView>(R.id.tvnum)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cardhistory, parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position:Int) {
        var kk = listdata.get(position)
        holder.tvbid.text=kk.bookid
        holder.tvbname.text=kk.name
        holder.tvbauthor.text=kk.author
        holder.tvbstatus.text=kk.Status
        holder.tvcdes.text=kk.date


        val uri= Uri.parse(kk.path.trim())
        Glide.with(context).load(uri).into(holder.img1)


        holder.linearuser.visibility=View.GONE

        holder.tvmoredetails.setOnClickListener {
            holder.linearuser.visibility=View.VISIBLE
            Handler().postDelayed({
                holder.linearuser.visibility=View.GONE
            },5000)
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.usersemail(kk.nemail,"emailuser")
                    .enqueue(object : Callback<Userresponse> {
                        override fun onResponse(call: Call<Userresponse>, response: Response<Userresponse>) {
                            val kkk=response.body()!!.user[0]
                            holder. tvname.text=kkk.name
                            holder.tvnum.text=kkk.moblie
                            holder.tvnum.setOnClickListener {
                                val intent = Intent(Intent.ACTION_CALL);
                                intent.data = Uri.parse("tel:${kkk.moblie}")
                                context.startActivity(intent)
                            }
                        }
                        override fun onFailure(call: Call<Userresponse>, t: Throwable) {
                            Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()

                        }

                    })
            }
        }

    }

    override fun getItemCount() = listdata.size
}