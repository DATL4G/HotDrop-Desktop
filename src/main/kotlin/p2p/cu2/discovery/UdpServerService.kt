package p2p.cu2.discovery

import p2p.cu2.model.Host
import java.io.IOException
import java.net.*
import java.util.*


class UdpServerService {

    private var socket: DatagramSocket? = null
    private val hostHandlerMap: MutableMap<Host, StaleHostHandler?> = mutableMapOf()
    private val currentHostIps: MutableSet<String> = Collections.synchronizedSet(mutableSetOf())
    private var isHostClientToo = false
    private var listener: UdpBroadcastListener? = null
    private var timer = Timer()

    private fun initCurrentDeviceIps() {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            val updatedIps: MutableSet<String> = mutableSetOf()

            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()

                try {
                    if(networkInterface.isLoopback || !networkInterface.isUp) {
                        continue
                    }

                    val inetAddresses = networkInterface.inetAddresses
                    while (inetAddresses.hasMoreElements()) {
                        val inetAddress = inetAddresses.nextElement()
                        updatedIps.add(inetAddress.hostAddress)
                    }
                } catch (e: SocketException) {}
            }
            currentHostIps.retainAll(updatedIps)
            currentHostIps.addAll(updatedIps)
        } catch (e: SocketException) {}
    }

    private fun updateListener(listener: UdpBroadcastListener) {
        this.listener = listener
        synchronized(this) {
            hostHandlerMap.values.forEach {
                it?.setListener(listener)
            }
        }
    }

    fun start(interval: Long, listener: UdpBroadcastListener) {
        initCurrentDeviceIps()
        updateListener(listener)
        timer.schedule(object: TimerTask(){
            override fun run() {
                broadcast()
            }
        }, 0, interval)
    }

    fun stop() {
        socket?.close()
        socket = null
        listener = null
        timer.cancel()
    }

    private fun broadcast() {
        try {
            if(socket == null) {
                socket = DatagramSocket(null)
                val socketAddress = InetSocketAddress(InetAddress.getByName("0.0.0.0"), 8888)
                socket!!.reuseAddress = true
                socket!!.broadcast = true
                socket!!.soTimeout = 0
                socket!!.bind(socketAddress)
            }

            val recvBuf = ByteArray(15000)
            val packet = DatagramPacket(recvBuf, recvBuf.size)
            val recSocket = socket
            recSocket?.receive(packet)

            val host = Host(packet.address, String(packet.data).trim())

            if(isHostClientToo || !currentHostIps.contains(host.inetAddress.hostAddress)) {
                var handler = hostHandlerMap[host]
                if(handler == null) {
                    handler = StaleHostHandler(host, hostHandlerMap, listener)
                    synchronized(this){
                        hostHandlerMap.put(host, handler)
                    }
                    listener?.onHostUpdate(hostHandlerMap.keys)
                } else if(hostNameChanged(host, hostHandlerMap)) {
                    listener?.onHostUpdate(hostHandlerMap.keys)
                }
                stop()
            }
        } catch (e: SocketException) {
            socket?.let {
                it.close()
                socket = null
            }
        } catch (e: UnknownHostException) {
            socket?.let {
                it.close()
                socket = null
            }
        } catch (e: IOException) {
            listener?.onReceiveFail()
        }
    }

    private fun hostNameChanged(updatedHost: Host, hostHandler: MutableMap<Host, StaleHostHandler?>): Boolean {
        hostHandler.forEach { (host, _) ->
            if (updatedHost == host && updatedHost.name != host.name) {
                hostHandlerMap[updatedHost] = hostHandlerMap.remove(host)
            }
        }
        return false
    }

    private class StaleHostHandler(private val host: Host,
                                        private val hostHandlerMap: MutableMap<Host, StaleHostHandler?>,
                                        private var listener: UdpBroadcastListener?) {

        init {
            hostUpdate()
        }

        fun hostUpdate() {
            hostHandlerMap.remove(host)
            listener?.onHostUpdate(hostHandlerMap.keys)
        }

        fun setListener(listener: UdpBroadcastListener) {
            this.listener = listener
        }

    }

    interface UdpBroadcastListener {
        fun onServerSetupFailed(e: Throwable)
        fun onReceiveFail()
        fun onHostUpdate(currentHosts: Set<Host>)
    }
}