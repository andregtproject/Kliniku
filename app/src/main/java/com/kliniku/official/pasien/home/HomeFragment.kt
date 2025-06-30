package com.kliniku.official.pasien.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kliniku.official.databinding.FragmentHomeBinding

import com.kliniku.official.R

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var clinicAdapter: ClinicAdapter
    private lateinit var doctorAdapter: DoctorAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupRecyclerView()
    }

    private fun setupViews() {
        binding.searchContainer.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        val listClinic = listOf(
            ClinicModel("Aliyah MEDIKA", "Klinik Umum", "Jl. Sukabirus No.37", "3 km", "15-20mnt", 4.8, R.drawable.img_clinic1),
            ClinicModel("Klinik Sehat", "Klinik Gigi", "Jl. Merdeka No.10", "1.2 km", "10mnt", 4.6, R.drawable.img_clinic2)
        )
        val listDoctor = listOf(
            DoctorModel("dr. Susi Sanjaya", "Dokter Umum", 4.8, R.drawable.img_doctor),
            DoctorModel("drg. Ira Sukamti", "Dokter", 4.3, R.drawable.img_doctor)
        )
        val listCategory = listOf(
            CategoryModel("Umum", R.drawable.ic_star),
            CategoryModel("Gigi", R.drawable.ic_star),
            CategoryModel("Kecantikan", R.drawable.ic_star),
            CategoryModel("Anak", R.drawable.ic_star),
            CategoryModel("Kulit", R.drawable.ic_star)
        )


        clinicAdapter = ClinicAdapter(listClinic)
        doctorAdapter = DoctorAdapter(listDoctor)
        categoryAdapter = CategoryAdapter(listCategory)


        binding.recyclerClinic.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = clinicAdapter
        }

        binding.recyclerDoctor.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = doctorAdapter
        }

        binding.recyclerCategory.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.adapter = categoryAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
