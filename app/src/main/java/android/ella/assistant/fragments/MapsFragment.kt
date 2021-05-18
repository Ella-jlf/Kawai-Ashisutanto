package android.ella.assistant.fragments

import android.ella.assistant.R
import android.ella.assistant.entity.Assistant
import android.ella.assistant.viewmodel.ListViewModel
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private val viewModel: ListViewModel by activityViewModels()
    private val hashMapMarkers = HashMap<Marker,Assistant>()

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener(this)
        viewModel.getAssistantsLiveData().observe(viewLifecycleOwner,{
            loadMarkers()
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_container) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun loadMarkers(){
        for (marker in hashMapMarkers.keys){
            marker.remove()
        }
        hashMapMarkers.clear()
        for (i in viewModel.getAssistants()){
            if (i.latitude != null && i.longitude != null){
                val markerOptions = MarkerOptions().position(LatLng(i.latitude!!, i.longitude!!)).title(i.name)
                hashMapMarkers[mMap.addMarker(markerOptions)!!] = i
            }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        Toast.makeText(requireContext(),marker.title,Toast.LENGTH_SHORT).show()
        viewModel.assistantPos.value = viewModel.getAssistants().indexOf(hashMapMarkers[marker])
        parentFragmentManager.commit {
            replace<RepresentFragment>(R.id.fragment_container)
            addToBackStack(null)
        }
        return true
    }

}