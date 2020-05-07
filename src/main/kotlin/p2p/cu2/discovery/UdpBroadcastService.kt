package p2p.cu2.discovery

import org.json.JSONObject
import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

class UdpBroadcastService {

    private lateinit var hostJson: JSONObject
    private var socket: DatagramSocket? = null
    private var port: Int = 8888
    private var timer = Timer()

    init {
        try {
            socket = DatagramSocket()
            socket!!.broadcast = true
        } catch (e: Exception) { e.printStackTrace() }
    }

    fun start(hostJson: JSONObject, interval: Long, port: Int) {
        this.hostJson = hostJson
        this.port = port
        timer.schedule(object: TimerTask(){
            override fun run() {
                broadcast()
                println("Called timer, Client start")
            }
        }, 0, interval)
    }

    fun stop() {
        timer.cancel()
        socket?.close()
    }

    private fun broadcast() {
        if (socket != null) {
            try {
                val sendData = hostJson.toString().toByteArray()

                try {
                    val sendPacket = DatagramPacket(sendData, sendData.size, InetAddress.getByName("255.255.255.255"), port)
                    socket!!.send(sendPacket)
                } catch (e: Exception) {}

                val interfaces = NetworkInterface.getNetworkInterfaces()
                while (interfaces.hasMoreElements()) {
                    val networkInterface = interfaces.nextElement()

                    if (networkInterface.isLoopback || !networkInterface.isUp) {
                        continue
                    }

                    networkInterface.interfaceAddresses.forEach {
                        val broadcastAddress = it.broadcast

                        try {
                            val sendPacket = DatagramPacket(sendData, sendData.size, broadcastAddress, port)
                            socket!!.send(sendPacket)
                        } catch (e: Exception) {}
                    }
                }
            } catch (e: Exception) {}
        }
    }
}