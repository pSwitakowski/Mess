package com.example.mess.activities


//import com.example.mess.map.MapAdapter
//import kotlinx.android.synthetic.main.content_map.*
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mess.R
import com.example.mess.data.MapModel
import com.example.mess.map.MapClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import kotlinx.android.synthetic.main.activity_map.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.function.Predicate
import kotlin.collections.ArrayList


class MapActivity : AppCompatActivity() {

    lateinit var mapFragment : SupportMapFragment
    lateinit var pusher: Pusher
    val MY_PERMISSIONS_REQUEST_LOCATION = 100
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mapMarkers: ArrayList<MapModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MapActivity)
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment

        setupPusher()

        fab.setOnClickListener {
            if (checkLocationPermission())
                sendLocation()
        }
    }

    private fun addMarkers(googleMap: GoogleMap) {
        mapMarkers.forEach { place ->
            val latLng = LatLng(place.latitude, place.longitude)
            googleMap.clear()
            val marker = googleMap.addMarker((
                    MarkerOptions()
                        .position(latLng)
                        .title(place.username)
                    ))
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendLocation() {

        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location: Location? ->
                if (location!=null){
                    Log.e("TAG","location is not null")

                    val user = FirebaseAuth.getInstance().currentUser
                    val username = user?.email

                    val jsonObject = JSONObject()
                    jsonObject.put("latitude",location.latitude)
                    jsonObject.put("longitude",location.longitude)
                    jsonObject.put("username", username)

                    val body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
                    Log.e("TAG",jsonObject.toString())
                    MapClient().getClient().sendLocation(body).enqueue(object: Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {}

                        override fun onFailure(call: Call<String>?, t: Throwable) {
                            t.message?.let { Log.e("TAG", it) }
                        }
                    })

                } else {
                    Log.e("TAG","location is null")
                }
            }

    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                AlertDialog.Builder(this)
                    .setTitle("Location permission")
                    .setMessage("You need the location permission")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                        ActivityCompat.requestPermissions(this@MapActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION)
                    })
                    .create()
                    .show()


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION)
            }
            return false
        } else {
            return true
        }
    }

    override fun onStart() {
        super.onStart()
        pusher.connect()
    }

    override fun onStop() {
        super.onStop()
        pusher.disconnect()
    }


    private fun setupPusher() {

        Log.e("TAG","Pusher setup")
        val options = PusherOptions()
        options.setCluster("eu")
        pusher = Pusher(getString(R.string.pusher_api_key), options)

        val channel = pusher.subscribe("feed")

        channel.bind("location") { _, _, data ->
            val jsonObject = JSONObject(data)
            Log.d("TAG",data)

            val lat:Double = jsonObject.getString("latitude").toDouble()
            val lon:Double = jsonObject.getString("longitude").toDouble()
            val name:String = jsonObject.getString("username").toString()

            runOnUiThread {
                val model = MapModel(lat,lon,name)
                mapMarkers.add(model)

                mapMarkers.forEach { marker ->
                    println("${marker.latitude} ${marker.longitude} ${marker.username}")
                }

                mapFragment.getMapAsync {googleMap ->
                    addMarkers(googleMap)
                }
            }
        }
    }
}