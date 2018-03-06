package com.example.antonio.nearbyadvertiser

import android.util.Log
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*

class IoTNearbyConnectionCallback(var mainActivity: MainActivity):ConnectionLifecycleCallback() {

    private val TAG=javaClass.name;

    override fun onConnectionResult(p0: String?, result: ConnectionResolution?) {
        Log.d(TAG, "Connection Result!")
        when (result?.getStatus()?.getStatusCode()) {
            ConnectionsStatusCodes.STATUS_OK -> {
                mainActivity.onNearbyEndpointConnection(success = true,endpointId = p0!!)
                Log.d(TAG, "Connection OK!")
            }
            ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                mainActivity.onNearbyEndpointConnection(success = false,endpointId = p0!!)
            }
            else -> {
                mainActivity.onNearbyEndpointConnection(success = false,endpointId = p0!!)
            }
        }
    }

    override fun onDisconnected(p0: String?) {
        Log.d(TAG, "Disconnection!")

    }

    override fun onConnectionInitiated(p0: String?, p1: ConnectionInfo?) {
        Log.d(TAG, "Connection initialized!")
        Nearby.Connections.acceptConnection(mainActivity.GoogleClient, p0, mainActivity.payloadCallback);
    }
}