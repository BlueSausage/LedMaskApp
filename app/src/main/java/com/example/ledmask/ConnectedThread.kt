package com.example.ledmask

import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ConnectedThread(socket : BluetoothSocket) : Thread() {
    private var mmInStream : InputStream
    private var mmOutStream : OutputStream
    private val TAG = "LEDMASKBLUETOOTH"

    companion object RESPONSE_MESSAGE {
        const val RESPONSE_MESSAGE = 10
    }

    init {
        var tmpInStream : InputStream? = null
        var tmpOutStream : OutputStream? = null
        Log.i(TAG, "Creating thread")

        try {
            tmpInStream = socket.inputStream
            tmpOutStream = socket.outputStream
        } catch (e : IOException) {
            Log.e(TAG, "Error: ${e.message}")
        }

        mmInStream = tmpInStream!!
        mmOutStream = tmpOutStream!!

        try {
            mmOutStream.flush()
        } catch (e : IOException) {}
        Log.i(TAG,"IO's obtained")
    }

    fun write(bytes: ByteArray){
        try {
            Log.i(TAG, "Writing bytes")
            mmOutStream.write(bytes)
        } catch (e : IOException) {}
    }
}