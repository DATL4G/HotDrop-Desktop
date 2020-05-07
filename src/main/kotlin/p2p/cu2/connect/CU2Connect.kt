package p2p.cu2.connect

import p2p.cu2.discovery.CU2Discovery
import p2p.cu2.model.Host

interface CU2Connect {
    fun send(bytes: ByteArray, peer: Host): Long
    fun startReceiving()
    fun stopReceiving(abortCurrentTransfers: Boolean)
    fun getPeers(): Set<Host>
    fun isReceiving(): Boolean

    class Builder {
        private lateinit var listener: Listener
        private lateinit var peers: Set<Host>

        fun setListener(listener: Listener): Builder {
            this.listener = listener
            return this
        }

        fun fromDiscovery(discovery: CU2Discovery): Builder {
            peers = discovery.getAllAvaiablePeers()
            return this
        }

        fun forPeers(peers: Set<Host>): Builder {
            this.peers = peers
            return this
        }

        fun build(): CU2Connect {
            return CU2ConnectImpl(listener, peers)
        }
    }

    interface Listener {
        fun onReceive(bytes: ByteArray, sender: Host)
        fun onSendComplete(jobId: Long)
        fun onSendFailure(e: Throwable, jobId: Long)
        fun onStartListenFailure(e: Throwable)
    }
}