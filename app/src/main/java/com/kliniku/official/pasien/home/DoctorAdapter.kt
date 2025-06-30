package com.kliniku.official.pasien.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kliniku.official.databinding.ItemListClinicBinding
import com.kliniku.official.databinding.ItemListDoctorBinding

class DoctorAdapter (private val list: List<DoctorModel>) :
    RecyclerView.Adapter<DoctorAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemListDoctorBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val klinik = list[position]
        holder.binding.apply {
            imageDoctor.setImageResource(klinik.image)
            tvNameClinic.text = klinik.name
            tvTypeClinic.text = klinik.type
            tvRating.text = klinik.rating.toString()
        }
    }
}