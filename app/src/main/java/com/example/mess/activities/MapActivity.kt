package com.example.mess.activities


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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mess.R
import com.example.mess.data.MapModel
import com.example.mess.map.MapAdapter
import com.example.mess.map.MapClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.content_map.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapActivity : AppCompatActivity() {

    var adapter: MapAdapter = MapAdapter(this@MapActivity)
    lateinit var pusher: Pusher
    val MY_PERMISSIONS_REQUEST_LOCATION = 100
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        setupPusher()

        fab.setOnClickListener {
            if (checkLocationPermission())
                sendLocation()
        }

        with(map_content){
            layoutManager = LinearLayoutManager(this@MapActivity)
            adapter = this@MapActivity.adapter
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.e("Xd",location.toString())
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
                adapter.addItem(model)
            }
        }
    }
}