package com.example.antonio.nearbyadvertiser

import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient

class IoTGoogleApiClientCallback(var mainActivity: MainActivity): GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    private val TAG=javaClass.name;

    override fun onConnectionSuspended(p0: Int) {
        Log.d(TAG, "On Google Client Suspended")
    }

    override fun onConnected(p0: Bundle?) {
        Log.d(TAG, "On Google Client Connected")
        mainActivity.onGoogleApiResult(success = true)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.d(TAG, "On Google Client Connection failed")
        mainActivity.onGoogleApiResult(success = false)
    }
}