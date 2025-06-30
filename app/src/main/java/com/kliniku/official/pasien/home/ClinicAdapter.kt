package com.kliniku.official.pasien.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kliniku.official.databinding.ItemListClinicBinding

class ClinicAdapter(private val list: List<ClinicModel>) :
    RecyclerView.Adapter<ClinicAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemListClinicBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListClinicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val klinik = list[position]
        holder.binding.apply {
            imageKlinik.setImageResource(klinik.image)
            tvNameClinic.text = klinik.name
            tvTypeClinic.text = klinik.type
            tvAddress.text = klinik.address
            tvDistance.text = klinik.distance
            tvDuration.text = klinik.duration
            tvRating.text = klinik.rating.toString()
        }
    }
}