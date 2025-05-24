package com.kliniku.official.auth.profile_step

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.kliniku.official.R
import com.kliniku.official.databinding.FragmentAddressBinding
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.Locale

class AddressFragment : Fragment(), LocationListener {

    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!

    private var mapView: MapView? = null
    private var marker: Marker? = null
    private var locationManager: LocationManager? = null

    private val viewModel: CompleteProfileViewModel by activityViewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.entries.all { it.value }) {
            getCurrentLocation()
        } else {
            showToast(getString(R.string.failed_permission_address))
        }
    }

    companion object {
        private var isOsmConfigInitialized = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeOsmDroid()
    }

    private fun initializeOsmDroid() {
        if (!isOsmConfigInitialized) {
            val ctx = requireContext()
            Configuration.getInstance().apply {
                load(ctx, ctx.getSharedPreferences("osm_prefs", Context.MODE_PRIVATE))
                userAgentValue = ctx.packageName
            }
            isOsmConfigInitialized = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        setupMapView()
        setupClickListeners()
        restoreViewModelData()
    }

    private fun setupMapView() {
        mapView = binding.mapView.apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)

            controller.apply {
                setZoom(15.0)
                setCenter(GeoPoint(-6.9175, 107.6191)) // Bandung default
            }

            // Add tap listener for map clicks
            overlays.add(createMapTapOverlay())

            // Add scroll listener to keep info window visible
            addMapListener(object : MapListener {
                override fun onScroll(event: ScrollEvent?): Boolean {
                    marker?.showInfoWindow()
                    return true
                }

                override fun onZoom(event: ZoomEvent?): Boolean {
                    marker?.showInfoWindow()
                    return true
                }
            })
        }
    }

    private fun createMapTapOverlay() = object : org.osmdroid.views.overlay.Overlay() {
        override fun onSingleTapConfirmed(e: android.view.MotionEvent?, mapView: MapView?): Boolean {
            e?.let { event ->
                mapView?.let { map ->
                    val projection = map.projection
                    val geoPoint = projection.fromPixels(event.x.toInt(), event.y.toInt()) as GeoPoint
                    setLocationMarker(geoPoint)
                }
            }
            return true
        }
    }

    private fun setupClickListeners() {
        binding.fabMyLocation.setOnClickListener {
            checkLocationPermission()
        }
    }

    private fun restoreViewModelData() {
        viewModel.selectedGeoPoint?.let { geoPoint ->
            setLocationMarker(geoPoint)
        }
        viewModel.selectedAddress?.let { address ->
            binding.etAddress.setText(address)
        }
    }

    private fun checkLocationPermission() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else {
            requestPermissionLauncher.launch(permissions)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val locationMgr = locationManager ?: return

        binding.progressBar.visibility = View.VISIBLE

        val providers = locationMgr.getProviders(true)
        if (providers.isEmpty()) {
            showToast(getString(R.string.gps_activation))
            binding.progressBar.visibility = View.GONE
            return
        }

        var bestLocation: Location? = null
        for (provider in providers) {
            val location = locationMgr.getLastKnownLocation(provider)
            if (location != null && (bestLocation == null || location.accuracy < bestLocation.accuracy)) {
                bestLocation = location
            }
        }

        if (bestLocation != null) {
            onLocationChanged(bestLocation)
        } else {
            for (provider in providers) {
                locationMgr.requestLocationUpdates(provider, 0, 0f, this)
            }

            lifecycleScope.launch {
                kotlinx.coroutines.delay(10000)
                if (_binding?.progressBar?.visibility == View.VISIBLE) {
                    binding.progressBar.visibility = View.GONE
                    locationMgr.removeUpdates(this@AddressFragment)
                }
            }
        }
    }

    private fun setLocationMarker(geoPoint: GeoPoint) {
        val map = mapView ?: return

        marker?.let { map.overlays.remove(it) }

        marker = Marker(map).apply {
            position = geoPoint
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_mark_loc)

            // Add info window
            title = getString(R.string.map_window_info)
            setOnMarkerClickListener { _, _ ->
                showInfoWindow()
                true
            }
        }

        map.overlays.add(marker)
        map.invalidate()

        // Show info window immediately
        marker?.showInfoWindow()

        viewModel.selectedGeoPoint = geoPoint
        getAddressFromLocation(geoPoint.latitude, geoPoint.longitude)
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    var result: List<Address>? = null
                    geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                        result = addresses
                    }
                    var attempts = 0
                    while (result == null && attempts < 50) {
                        kotlinx.coroutines.delay(100)
                        attempts++
                    }
                    result
                } else {
                    @Suppress("DEPRECATION")
                    geocoder.getFromLocation(latitude, longitude, 1)
                }

                if (addresses?.isNotEmpty() == true) {
                    updateAddressField(addresses[0])
                } else {
                    updateAddressField(null)
                }
            } catch (e: Exception) {
                updateAddressField(null)
            }
        }
    }

    private fun updateAddressField(address: Address?) {
        if (_binding == null) return

        binding.progressBar.visibility = View.GONE

        val addressText = if (address != null) {
            buildString {
                for (i in 0..address.maxAddressLineIndex) {
                    if (i > 0) append("\n")
                    append(address.getAddressLine(i))
                }
            }
        } else {
            getString(R.string.failed_address)
        }

        binding.etAddress.setText(addressText)
        viewModel.selectedAddress = addressText
    }

    override fun onLocationChanged(location: Location) {
        locationManager?.removeUpdates(this)
        binding.progressBar.visibility = View.GONE

        val geoPoint = GeoPoint(location.latitude, location.longitude)

        mapView?.controller?.apply {
            val currentZoom = mapView?.zoomLevelDouble ?: 17.0
            animateTo(geoPoint)
            setZoom(currentZoom)
        }

        setLocationMarker(geoPoint)

        mapView?.postDelayed({
            marker?.showInfoWindow()
        }, 500)
    }

    override fun onProviderDisabled(provider: String) {
        binding.progressBar.visibility = View.GONE
    }

    override fun onProviderEnabled(provider: String) {}

    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
        locationManager?.removeUpdates(this)
    }

    override fun onDestroyView() {
        locationManager?.removeUpdates(this)
        mapView?.let { map ->
            map.overlays.clear()
            map.onDetach()
        }
        mapView = null
        marker = null
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(message: String) {
        if (_binding != null && isAdded) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    fun isAddressSelected(): Boolean {
        return binding.etAddress.text.toString().trim().isNotEmpty()
    }

    fun refreshMap() {
        mapView?.let { map ->
            map.onResume()
            map.invalidate()
            restoreViewModelData()
        }
    }
}