package com.kliniku.official.auth.profile_step

import android.net.Uri
import androidx.lifecycle.ViewModel
import org.osmdroid.util.GeoPoint

class CompleteProfileViewModel : ViewModel() {
    var selectedPhotoUri: Uri? = null
    var selectedGeoPoint: GeoPoint? = null
    var selectedAddress: String? = null
    var selectedRole: String? = null
    var selectedGender: String? = null
}
