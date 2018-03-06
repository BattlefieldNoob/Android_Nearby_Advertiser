package com.example.antonio.nearbyadvertiser

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintSet.PARENT_ID
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.ConstraintSetBuilder.Side.*
import org.jetbrains.anko.constraint.layout.applyConstraintSet
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainActivity : AppCompatActivity() {

    lateinit var GoogleClient: GoogleApiClient

    var ConnectionCallback = IoTNearbyConnectionCallback(this)

    var payloadCallback= IoTPayloadCallback(this)

    var googleApiClientCallback= IoTGoogleApiClientCallback(this)

    lateinit var eventsListViewAdapter: ArrayAdapter<String>

    lateinit var eventsListView: ListView

    lateinit var RaspberryID:String

    lateinit var sendButton:Button


    var TAG=javaClass.name

    var MY_PERMISSIONS_REQUEST_COARSE_LOCATION=1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GoogleClient = GoogleApiClient.Builder(this, googleApiClientCallback, googleApiClientCallback).addApi(Nearby.CONNECTIONS_API).build()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    MY_PERMISSIONS_REQUEST_COARSE_LOCATION)
        }else{
            GoogleClient.connect()
        }

        eventsListViewAdapter= ArrayAdapter(this,android.R.layout.simple_list_item_1)

        constraintLayout {

            eventsListView = listView {
                adapter=eventsListViewAdapter
            }

            linearLayout {
                orientation=LinearLayout.VERTICAL

                var editTextxx = editText(){
                    width=700
                }

                sendButton = button("Send") {
                    onClick { Nearby.Connections.sendPayload(GoogleClient, RaspberryID, Payload.fromBytes(editTextxx.text.toString().toByteArray())) }
                    isClickable=false
                    isActivated=false
                }
            }.setPadding(0,600,0,0)

            applyConstraintSet {
                eventsListView {
                    connect(
                            START to START of PARENT_ID,
                            END to END of PARENT_ID,
                            BOTTOM to BOTTOM of PARENT_ID,
                            TOP to TOP of PARENT_ID
                    )
                }
                /*editTextxx {
                    connect(
                            START to START of PARENT_ID,
                            END to END of PARENT_ID,
                            TOP to BOTTOM of hellotext
                    )
                }*/
                /*sendButton {
                    connect(
                            START to START of PARENT_ID,
                            END to END of PARENT_ID,
                            BOTTOM to BOTTOM of PARENT_ID,
                            TOP to TOP of PARENT_ID margin dip(32)
                    )
                    verticalBias=0.20f
                }*/
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Nearby.Connections.stopAdvertising(GoogleClient)
        GoogleClient.disconnect();
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_COARSE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    GoogleClient.connect()
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return
            }

        // Add other 'when' lines to check for other
        // permissions this app might request.

            else -> {
                // Ignore all other requests.
            }
        }
    }

    fun onGoogleApiResult(success:Boolean){
        if(success) {
            eventsListViewAdapter.add("Google Api Connected!")
            Nearby.Connections.startAdvertising(GoogleClient,"Cellphone", "Raspberry", ConnectionCallback, AdvertisingOptions(Strategy.P2P_CLUSTER)).setResultCallback {
                startAdvertisingResult ->
                Log.d(TAG,startAdvertisingResult.localEndpointName)
                eventsListViewAdapter.add("Advertising With Name:${startAdvertisingResult.localEndpointName}")
            }
        }else{
            Log.e(TAG, "Error!")
        }
    }

    fun onNearbyEndpointConnection(success: Boolean,endpointId: String){
        if (success) {
            eventsListViewAdapter.add("Connected to endpoint id:$endpointId")
            sendButton.isClickable=true
            sendButton.isActivated=true
            //Nearby.Connections.sendPayload(GoogleClient, endpointId, Payload.fromBytes("Hello!!".toByteArray()))
        }else{
            Log.d(TAG,"Error!")
        }
    }

    fun onPayloadReceived(endpointId: String, message:String){
        eventsListViewAdapter.add("From $endpointId, message:$message")
        RaspberryID=endpointId;
    }
}
