package com.example.antonio.nearbyadvertiser

import android.util.Log
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate

class IoTPayloadCallback(var mainActivity: MainActivity):PayloadCallback() {

    private val TAG=javaClass.name;

    override fun onPayloadReceived(p0: String?, p1: Payload?) {
        Log.d(TAG, "Received Payload:" + String(p1?.asBytes()!!))
        mainActivity.onPayloadReceived(p0!!, String(p1.asBytes()!!))
    }

    override fun onPayloadTransferUpdate(p0: String?, p1: PayloadTransferUpdate?) {
    }

}