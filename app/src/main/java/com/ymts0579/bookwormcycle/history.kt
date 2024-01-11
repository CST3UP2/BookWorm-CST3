package com.ymts0579.bookwormcycle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ymts0579.bookwormcycle.model.BooksResponse
import com.ymts0579.bookwormcycle.model.ExchangeResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class history : Fragment() {
     lateinit var listuserhistory:RecyclerView
    var email=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       var view=inflater.inflate(R.layout.fragment_history, container, false)
        listuserhistory=view.findViewById(R.id.listuserhistory)
        listuserhistory.layoutManager = LinearLayoutManager(context)
        listuserhistory.setHasFixedSize(true)
            requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE).apply {
            email=getString("email","").toString()

        }

        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.instance.viewexchangebooks(email,"nemail")
                .enqueue(object : Callback<ExchangeResponse> {
                    override fun onResponse(call: Call<ExchangeResponse>, response: Response<ExchangeResponse>) {

                       listuserhistory.adapter=historyadapter(requireActivity(),response.body()!!.user)
                       Toast.makeText(context,"${response.body()!!.message}",Toast.LENGTH_SHORT).show()

                    }

                    override fun onFailure(call: Call<ExchangeResponse>, t: Throwable) {
                        Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()

                    }

                })
        }
        return view
    }


}