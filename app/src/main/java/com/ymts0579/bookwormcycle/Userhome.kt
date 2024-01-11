package com.ymts0579.bookwormcycle

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout


class Userhome : Fragment() {
lateinit var addbooks:LinearLayout
lateinit var searchbooksbtn:LinearLayout
lateinit var btnviewrequest:LinearLayout
lateinit var linearviewbook:LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_userhome, container, false)
        addbooks=view.findViewById(R.id.addbooks)
        searchbooksbtn=view.findViewById(R.id.searchbooksbtn)
        btnviewrequest=view.findViewById(R.id.btnviewrequest)
        linearviewbook=view.findViewById(R.id.linearviewbook)
        addbooks.setOnClickListener { startActivity(Intent(requireActivity(),AddBooks::class.java)) }

        searchbooksbtn.setOnClickListener { startActivity(Intent(requireActivity(),SearchBooks::class.java)) }

        btnviewrequest.setOnClickListener { startActivity(Intent(requireActivity(),ViewRequest::class.java)) }

        linearviewbook.setOnClickListener {
            startActivity(
                Intent(requireActivity(), Viewbooks::class.java)
            )
        }
        return view
    }


}