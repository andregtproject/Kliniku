package com.kliniku.official.beranda

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.kliniku.official.R
import com.kliniku.official.pasien.home.SearchActivity


class BerandaFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_beranda, container, false)
        setupSearchClickListener(view)
        return view
    }

    private fun setupSearchClickListener(view: View) {
        val searchContainer = view.findViewById<LinearLayout>(R.id.searchContainer)
        val etSearch = view.findViewById<EditText>(R.id.etSearch)


        searchContainer.setOnClickListener { openSearchActivity() }


        etSearch.setOnClickListener { openSearchActivity() }
    }

    private fun openSearchActivity() {
        val intent = Intent(requireContext(), SearchActivity::class.java)
        startActivity(intent)
    }
}