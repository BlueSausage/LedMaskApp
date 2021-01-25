package com.example.ledmask

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    private val BLUETOOTHTAG = "LEDMASKBLUETOOTH"
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private lateinit var mmSocket: BluetoothSocket
    private lateinit var mmDevice: BluetoothDevice
    private var bluetoothConnectedThread: ConnectedThread? = null
    private val REQUEST_ENABLE_BT = 1
    private val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (bluetoothAdapter == null) {
            Log.i(BLUETOOTHTAG, "Device doesn't support Bluetooth")
            //Todo disable buttons
        } else {
            Log.i(BLUETOOTHTAG, "Device supports Bluetooth")
            connectMask()
        }

        findViewById<Button>(R.id.createNewImageBtn).setOnClickListener {
            val newImageIntent = Intent(this, CreateNewImageActivity::class.java)
            startActivity(newImageIntent)
        }
    }

    private fun connectMask() {
        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
        if (bluetoothAdapter?.isEnabled == true) {
            val tmp: BluetoothSocket?
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
            pairedDevices?.forEach { device ->
                val deviceName = device.name
                val deviceHardwareAddress = device.address
                if (deviceName == "Led-Mask") {
                    Log.i(BLUETOOTHTAG, "Device found: $deviceName -> $deviceHardwareAddress")
                    mmDevice = bluetoothAdapter.getRemoteDevice(deviceHardwareAddress)
                }
            }

            try {
                Log.i(BLUETOOTHTAG, "UUID: $MY_UUID")
                tmp = mmDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID)
                mmSocket = tmp
                mmSocket.connect()
                Log.i(BLUETOOTHTAG, "Connected to: ${mmDevice.name}")
            } catch (e: IOException) {
                Log.i(BLUETOOTHTAG, "Can't connect to ${mmDevice.name} because ${e.message}")
                try {
                    mmSocket.close()
                } catch (c: IOException) {
                    return
                }
            }

            Log.i(BLUETOOTHTAG, "Creating and running Thread")
            bluetoothConnectedThread = ConnectedThread(mmSocket)
            bluetoothConnectedThread!!.start()
        }
    }
}