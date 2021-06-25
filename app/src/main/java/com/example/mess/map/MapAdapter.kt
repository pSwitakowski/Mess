package com.example.mess.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mess.R
import com.example.mess.data.MapModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapAdapter(private val mContext: AppCompatActivity)
    : RecyclerView.Adapter<MapAdapter.MyViewHolder>() {

    private var arrayList: ArrayList<MapModel> = ArrayList()

    override fun getItemCount(): Int {
        return arrayList.size
    }
    override fun onBindViewHolder(holder: MapAdapter.MyViewHolder, position: Int) {
        val latLng = LatLng(arrayList[position].latitude,arrayList[position].longitude)

        holder.mapView.onCreate(null)
        holder.mapView.getMapAsync(OnMapReadyCallback {
            it.addMarker(
                MarkerOptions()
                    .title(arrayList[position].username)
                    .position(latLng))
            val cameraPosition = CameraPosition.Builder().target(latLng).zoom(17f).build()
            it.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.custom_map_view, parent, false)
        return MyViewHolder(view)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mapView: MapView = itemView.findViewById(R.id.map)
    }

    fun addItem(model: MapModel) {
        this.arrayList.add(model)
        notifyDataSetChanged()
    }
}