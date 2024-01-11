package com.ymts0579.bookwormcycle

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.telephony.gsm.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

import com.ymts0579.bookwormcycle.model.Books
import com.ymts0579.bookwormcycle.model.ExchangeResponse
import com.ymts0579.bookwormcycle.model.exchange
import com.ymts0579.fooddonationapp.model.Userresponse
import com.ymts0579.model.model.DefaultResponse

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewRequest : AppCompatActivity() {
    lateinit var listuserrequest:RecyclerView
    var email=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_request)
        listuserrequest=findViewById(R.id.listuserrequest)
        listuserrequest.layoutManager = LinearLayoutManager(this)
        listuserrequest.setHasFixedSize(true)

        getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            email=getString("email","").toString()

        }


        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.viewexchangebooksrequest(email,"oemail")
                .enqueue(object : Callback<ExchangeResponse> {
                    override fun onResponse(call: Call<ExchangeResponse>, response: Response<ExchangeResponse>) {

                        listuserrequest.adapter=viewrequestadapter(this@ViewRequest,response.body()!!.user)
                        Toast.makeText(this@ViewRequest,"${response.body()!!.message}",Toast.LENGTH_SHORT).show()

                    }

                    override fun onFailure(call: Call<ExchangeResponse>, t: Throwable) {
                        Toast.makeText(this@ViewRequest, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }

    }



    class viewrequestadapter(var context: Context, var listdata: ArrayList<exchange>):
        RecyclerView.Adapter<viewrequestadapter.DataViewHolder>(){
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
            val view = LayoutInflater.from(context).inflate(R.layout.cardrequest, parent, false)
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
                            @SuppressLint("SuspiciousIndentation")
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

            if(kk.Status=="Returned"){
                Toast.makeText(context, " Book Already returned to you", Toast.LENGTH_SHORT).show()
            }else{
                holder.itemView.setOnClickListener {
                    id=listdata[position].id
                    var alertdialog= AlertDialog.Builder(context)
                    alertdialog.setIcon(R.drawable.ic_launcher_foreground)
                    alertdialog.setTitle("Accept/Reject/Returned")
                    alertdialog.setIcon(R.drawable.logo)
                    alertdialog.setCancelable(false)
                    alertdialog.setMessage("Are you Accept or Reject or Returned the request?")
                    alertdialog.setPositiveButton("Accept"){ alertdialog, which->
                        Acceptedrequest(id,holder.tvnum.text.toString(),"Accepted")
                        alertdialog.dismiss()
                    }
                    alertdialog.setNegativeButton("Reject"){alertdialog,which->
                        request(id,holder.tvnum.text.toString(),"Rejected",kk.bookid)
                        alertdialog.dismiss()
                    }

                    alertdialog.setNeutralButton("Returned"){alertdialog,which->
                        request(id,holder.tvnum.text.toString(),"Returned",kk.bookid)
                        alertdialog.dismiss()
                    }
                    alertdialog.show()

                }
            }






        }

        private fun Acceptedrequest(id: Int, toString: String, s: String) {
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.updateexchangebooks(id,s,"userbookselected")
                    .enqueue(object: Callback<DefaultResponse> {
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            Toast.makeText(context, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                            val smsManager: SmsManager = SmsManager.getDefault()
                            smsManager.sendTextMessage(toString
                                , null, "your Request is ${s}", null, null)

                        }
                    })
            }
        }

        private fun request(id: Int, number: String,status: String,bookid:String) {
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.instance.updateexchange(id,status,"updatebookselected",bookid)
                    .enqueue(object: Callback<DefaultResponse> {
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            Toast.makeText(context, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                            val smsManager: SmsManager = SmsManager.getDefault()
                            smsManager.sendTextMessage(number
                                , null, "your Request is ${status} for ${bookid}", null, null)
                        }
                    })
            }
        }


        override fun getItemCount() = listdata.size
    }
}

