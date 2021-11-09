package com.dave.survey

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_view_beacons.*
import kotlinx.android.synthetic.main.bottom_sheet_persistent.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.overlay.Marker
import java.util.*
import kotlin.collections.ArrayList

class ViewBeacons : AppCompatActivity() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("survey").child("beacon_details")

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_beacons)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        //#3 Listening to State Changes of BottomSheet
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

//                if (newState == BottomSheetBehavior.STATE_EXPANDED){
//                    MyBottomSheetDialogFragment().apply {
//                        show(supportFragmentManager, tag)
//                    }
//                }

//                buttonBottomSheetPersistent.text = when (newState) {
//                    BottomSheetBehavior.STATE_EXPANDED -> "Close Persistent Bottom Sheet"
//                    BottomSheetBehavior.STATE_COLLAPSED -> "Open Persistent Bottom Sheet"
//                    else -> "Persistent Bottom Sheet"
//                }
            }
        })



        //#4 Changing the BottomSheet State on ButtonClick
//        buttonBottomSheetPersistent.setOnClickListener {
//            val state =
//                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
//                    BottomSheetBehavior.STATE_COLLAPSED
//                else
//                    BottomSheetBehavior.STATE_EXPANDED
//            bottomSheetBehavior.state = state
//        }

        initMap()
    }

    private fun getData(beaconDetailsList: ArrayList<BeaconData>) {

        recyclerView.setHasFixedSize(true);
        recyclerView.layoutManager = LinearLayoutManager(this);

        val beaconsAdapter = BeaconsAdapter(
            beaconDetailsList, this
        )
        recyclerView.adapter = beaconsAdapter
    }

    private fun initMap() {

        val StartLatitude = "-1.292066".toDouble()
        val StartLongitude = "36.821945".toDouble()

        val geoPoint = GeoPoint(StartLatitude, StartLongitude)
        map.setTileSource(TileSourceFactory.OpenTopo)
        map.setMultiTouchControls(true)
        map.controller.setZoom(4.0)
        map.setMaxZoomLevel(null)
        val mapController = map.controller as MapController
//        mapController.animateTo(geoPoint, 12.5, 9000L)
        mapController.setZoom(7.5)
        mapController.setCenter(geoPoint)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val recyclerBinList = ArrayList<BeaconData>()
                for (childDataSnapshot in dataSnapshot.children) {
                    val keys = childDataSnapshot.key!!
                    val dataSnapshot1 = dataSnapshot.child(keys)
                    val beaconData = dataSnapshot1.getValue(BeaconDetails::class.java)!!

                    val beaconDetails = BeaconData(
                        keys,
                        beaconData.beaconName,
                        beaconData.beaconDetails,
                        beaconData.additionalDetails,
                        beaconData.imageUrl,
                        beaconData.latitude,
                        beaconData.longitude)

                    recyclerBinList.add(beaconDetails)
                }
                for (i in recyclerBinList.indices) {

                    val controlName: String = recyclerBinList[i].beaconName
                    val controlDetails: String = recyclerBinList[i].beaconDetails
                    val latitude: String = recyclerBinList[i].latitude
                    val longitude: String = recyclerBinList[i].longitude
                    val controlMoreDetails: String = recyclerBinList[i].additionalDetails
                    val imageUrl: String = recyclerBinList[i].imageUrl

                    val lat =if (latitude.isNotEmpty()) {
                         latitude.toDouble()
                    }else{
                        0.0
                    }

                    val lon =if (longitude.isNotEmpty()){
                         longitude.toDouble()
                    }else{
                        0.0
                    }



                    val markerDetails = """
                    Control Name : $controlName
                    Control Details : $controlDetails 
                    Control More Details : $controlMoreDetails
                    """.trimIndent()

                    val geoPoint3 = GeoPoint(lat, lon)
                    val marker = Marker(map)
                    marker.position = geoPoint3
                    map.overlays.add(marker)
                    marker.title = markerDetails
                    marker.icon = resources.getDrawable(R.drawable.ic_action_recycler)
                }

                getData(recyclerBinList)

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

}