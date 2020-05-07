package p2p.cu2.discovery

import p2p.cu2.model.Host
import java.lang.Exception
import java.net.DatagramSocket
import java.net.NetworkInterface
import java.util.*

class UdpServerService {

    private var socket: DatagramSocket? = null
    private val hostHandlerMap: MutableMap<Host, StaleHostHandler?> = mutableMapOf()
    private val currentHostIps: MutableSet<String> = Collections.synchronizedSet(mutableSetOf())
    private var isHostClientToo = false
    private var listener: UdpBroadcastListener? = null
    private var timer = Timer()
    private var port = 8888

    private fun initCurrentDeviceIps() {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            val updatedIps: MutableSet<String> = mutableSetOf()

            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()

                try {
                    if (networkInterface.isLoopback || !networkInterface.isUp) {
                        continue
                    }

                    val inetAddresses = networkInterface.inetAddresses
                    while (inetAddresses.hasMoreElements()) {
                        val inetAddress = inetAddresses.nextElement()
                        updatedIps.add(inetAddress.hostAddress)
                    }
                } catch (e: Exception) {}
            }
            currentHostIps.retainAll(updatedIps)
            currentHostIps.addAll(updatedIps)
        } catch (e: Exception) {}
    }

    private fun updateListener(listener: UdpBroadcastListener) {
        this.listener = listener
        synchronized(this) {
            hostHandlerMap.values.forEach {
                it?.setListener(listener)
            }
        }
    }

    fun start(interval: Long, port: Int, listener: UdpBroadcastListener) {
        this.port = port
        initCurrentDeviceIps()
        updateListener(listener)
        timer.schedule(object: TimerTask(){
            override fun run() {
                broadcast()
                println("Called timer, Server start")
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

    }

    private fun hostNameChanged(updatedHost: Host, hostHandler: MutableMap<Host, StaleHostHandler?>): Boolean {
        hostHandlerMap.forEach { (host, _) ->
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