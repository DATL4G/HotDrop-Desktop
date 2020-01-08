package p2p.cu2.discovery

import p2p.cu2.model.Host

interface CU2Discovery {
    fun makeDiscoverable(hostName: String)
    fun makeNonDiscoverable()
    fun startDiscovery()
    fun stopDiscovery()
    fun getAllAvaiablePeers(): Set<Host>
    fun isDiscoverable(): Boolean
    fun isDiscovering(): Boolean

    class Builder {
        private var discoverableTimeout: Long = 60000
        private var discoveryTimeout: Long = 60000
        private var discoverablePingInterval: Long = 5000
        private lateinit var listener: Listener

        fun setDiscoverableTimeout(discoverableTimeout: Long): Builder {
            this.discoverableTimeout = discoverableTimeout
            return this
        }

        fun setDiscoveryTimeout(discoveryTimeout: Long): Builder {
            this.discoveryTimeout = discoveryTimeout
            return this
        }

        fun setDiscoverablePingInterval(discoverablePingInterval: Long): Builder {
            this.discoverablePingInterval = discoverablePingInterval
            return this
        }

        fun setDiscoveryListener(listener: Listener): Builder {
            this.listener = listener
            return this
        }

        fun build(): CU2Discovery {
            return CU2DiscoveryImpl(discoverableTimeout, discoveryTimeout, discoverablePingInterval, listener)
        }
    }

    interface Listener {
        fun onPeersUpdate(hosts: Set<Host>)
        fun onDiscoveryTimeout()
        fun onDiscoveryFailure(e: Throwable)
        fun onDiscoverableTimeout()
    }
}