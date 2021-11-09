package com.dave.survey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_beacons.*
import kotlinx.android.synthetic.main.activity_edit_details.*
import kotlinx.android.synthetic.main.activity_edit_details.etAdditionalDetails
import kotlinx.android.synthetic.main.activity_edit_details.etBeaconDetails
import kotlinx.android.synthetic.main.activity_edit_details.etBeaconName
import kotlinx.android.synthetic.main.activity_edit_details.imageView
import kotlinx.android.synthetic.main.activity_view_beacons.*
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

class EditDetails : AppCompatActivity() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("survey").child("beacon_details")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_details)

        val preferencesDetails = SharedPreferenceStorage(
            this,
            "beacons"
        )

        val getVerify = preferencesDetails.getSavedData("beacons")
        val firebase_key = getVerify["firebase_key"].toString()

        myRef.child(firebase_key).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val beaconData = dataSnapshot.getValue(BeaconDetails::class.java)!!

                etBeaconName.setText(beaconData.beaconName)
                etBeaconDetails.setText(beaconData.beaconDetails)
                etAdditionalDetails.setText(beaconData.additionalDetails)

                Picasso.get().load(beaconData.imageUrl).into(imageView)
                
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }
}