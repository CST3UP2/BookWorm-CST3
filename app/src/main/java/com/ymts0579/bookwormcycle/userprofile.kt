package com.ymts0579.bookwormcycle

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.ymts0579.model.model.DefaultResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class userprofile : Fragment() {
    lateinit var ethemail1: EditText
    lateinit var ethname1: EditText
    lateinit var ethnum1: EditText
    lateinit var ethaddress1: EditText
    lateinit var ethcity1: EditText
    lateinit var ethpass1: EditText
    lateinit var btnupdate: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_userprofile, container, false)
        ethemail1=view.findViewById(R.id.ethemail1)
        ethname1=view.findViewById(R.id.ethname1)
        ethnum1=view.findViewById(R.id.ethnum1)
        ethaddress1=view.findViewById(R.id.ethaddress1)
        ethcity1=view.findViewById(R.id.ethcity1)
        ethpass1=view.findViewById(R.id.ethpass1)
        btnupdate=view.findViewById(R.id.btnupdate)


        var id = 0;
        requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            id = getInt("id", 0)
            ethname1.setText(getString("name", "").toString())
            ethnum1.setText(getString("mob", "").toString())
            ethpass1.setText(getString("pass", "").toString())
            ethemail1.setText(getString("email", "").toString())
            ethemail1.setText(getString("email", "").toString())
            ethcity1.setText(getString("city", "").toString())
            ethaddress1.setText(getString("address", "").toString())

        }




        btnupdate.setOnClickListener {
            val name = ethname1.text.toString()
            val num = ethnum1.text.toString()
            val add = ethaddress1.text.toString()
            val pass = ethpass1.text.toString()
            var city = ethcity1.text.toString()

            if (num.count() == 10) {

                CoroutineScope(Dispatchers.IO).launch {
                    RetrofitClient.instance.updateusers(
                        "$name", "$num", "$pass",
                        "$add", "$city", id, "update"
                    )
                        .enqueue(object : Callback<DefaultResponse> {
                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                t.message?.let { it1 ->
                                    Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).show()
                                }
                            }

                            override fun onResponse(
                                call: Call<DefaultResponse>,
                                response: Response<DefaultResponse>
                            ) {
                                Toast.makeText(
                                    context,
                                    response.body()!!.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(activity, Login::class.java))

                            }
                        })
                }

                //Toast.makeText(activity, "$name,$num,$add,$city", Toast.LENGTH_SHORT).show()
            } else {
                ethnum1.setError("Enter Ngo number properly")
                Toast.makeText(activity, "Enter Ngo number properly", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return view
    }


}