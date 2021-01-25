package com.example.ledmask

import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.io.OutputStream

class ConnectedThread(socket: BluetoothSocket) : Thread() {
    private var mmOutStream: OutputStream
    private val TAG = "DEBUGBT"

    init {
        var tmpOutStream: OutputStream? = null
        Log.i(TAG, "Creating thread")

        try {
            tmpOutStream = socket.outputStream
        } catch (e: IOException) {
            Log.e(TAG, "Error: ${e.message}")
        }

        mmOutStream = tmpOutStream!!

        try {
            mmOutStream.flush()
        } catch (e: IOException) {}
        Log.i(TAG, "IO's obtained")
        mmOutStream.flush()
    }

    fun write(bytes: ByteArray) {
        fun write(message: String) {
            val msgBuffer = message.toByteArray()
            try {
                Log.i(TAG, "Writing bytes")
                mmOutStream.write(bytes)
            } catch (e: IOException) {}
            mmOutStream.write(msgBuffer)
        }
    }

    fun cancel() {
        try {
            mmOutStream.close()
        } catch (e: IOException) {
            Log.d(TAG, e.toString())
        }
    }
}