package p2p.cu2.discovery

import p2p.cu2.model.Host

interface CU2Discovery {
    fun makeDiscoverable(hostName: String, mustMatch: String = "")
    fun makeNonDiscoverable()
    fun startDiscovery()
    fun stopDiscovery()
    val allAvailablePeers: Set<Host>
    val isDiscoverable: Boolean
    val isDiscovering: Boolean

    class Builder {
        var mDiscoverableTimeout: Long = 60000
        var mDiscoveryTimeout: Long = 60000
        var mDiscoverablePingInterval: Long = 5000
        lateinit var mListener: Listener
        var mPort: Int = 8888
        var mRegex: Regex = Regex("^$")

        fun setDiscoverableTimeoutMillis(discoverableTimeout: Long): Builder {
            mDiscoverableTimeout = discoverableTimeout
            return this
        }

        fun setDiscoveryTimeoutMillis(discoveryTimeout: Long): Builder {
            mDiscoveryTimeout = discoveryTimeout
            return this
        }

        fun setDiscoveryPingIntervalMillis(discoveryPingInterval: Long): Builder {
            mDiscoverablePingInterval = discoveryPingInterval
            return this
        }

        fun setDiscoveryListener(listener: Listener): Builder {
            mListener = listener
            return this
        }

        fun setPort(port: Int): Builder {
            mPort = port
            return this
        }

        fun setFilter(regex: Regex): Builder {
            mRegex = regex
            return this
        }

        fun build(): CU2Discovery {
            return CU2DiscoveryImpl(mDiscoverableTimeout, mDiscoveryTimeout, mDiscoverablePingInterval, mListener, mPort, mRegex)
        }
    }

    interface Listener {
        fun onPeersUpdate(hosts: Set<Host>)
        fun onDiscoveryTimeout()
        fun onDiscoveryFailure(e: Throwable)
        fun onDiscoverableTimeout()
    }
}