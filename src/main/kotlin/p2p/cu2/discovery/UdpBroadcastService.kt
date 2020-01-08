package p2p.cu2.discovery

import java.io.IOException
import java.lang.Exception
import java.net.*
import java.util.*

class UdpBroadcastService {

    private var hostName: String = InetAddress.getLocalHost().hostName
    private var socket: DatagramSocket? = null
    private var timer = Timer()

    init {
        try {
            socket = DatagramSocket()
            socket!!.broadcast = true
        } catch (e: SocketException) {
            e.printStackTrace()
        }
    }

    fun start(hostName: String, interval: Long) {
        this.hostName = hostName
        timer.schedule(object: TimerTask(){
            override fun run() {
                broadcast()
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
                val sendData = hostName.toByteArray()

                try {
                    val sendPacket = DatagramPacket(sendData, sendData.size, InetAddress.getByName("255.255.255.255"), 8888)
                    socket!!.send(sendPacket)
                } catch (e: Exception) {}

                val interfaces = NetworkInterface.getNetworkInterfaces()
                while (interfaces.hasMoreElements()) {
                    val networkInterface = interfaces.nextElement()

                    if(networkInterface.isLoopback || !networkInterface.isUp) {
                        continue
                    }

                    networkInterface.interfaceAddresses.forEach {
                        val broadcastAddress = it.broadcast

                        try {
                            val sendPacket = DatagramPacket(sendData, sendData.size, broadcastAddress, 8888)
                            socket!!.send(sendPacket)
                        } catch (e: Exception) {}
                    }
                }
            } catch (e: IOException) {}
        }
    }
}