package com.kliniku.official.pasien.home

data class ClinicModel(
    val name: String,
    val type: String,
    val address: String,
    val distance: String,
    val duration: String,
    val rating: Double,
    val image: Int
)
