package p2p.cu2.connect

import p2p.cu2.model.Host

class CU2ConnectImpl(private val listener: CU2Connect.Listener,
                     private val peers: Set<Host>) : CU2Connect {

    private var serverState = false
    private val sendDataQueue: MutableList<ByteArray> = mutableListOf()
    private val sendDestQueue: MutableList<Host> = mutableListOf()
    private val sendJobQueue: MutableList<Long> = mutableListOf()
    private lateinit var clientServiceListener: TcpServerService.TcpServerListener
    private lateinit var serverServiceListener: TcpServerService.TcpServerListener



    private fun startServerConnection() {
    }

    private fun stopServer() {

    }

    override fun send(bytes: ByteArray, peer: Host): Long {
        val jobId = System.currentTimeMillis()
        synchronized(this) {
            sendDataQueue.add(bytes)
            sendDestQueue.add(peer)
            sendJobQueue.add(jobId)
        }

        //TODO("clientservice")

        return jobId
    }

    override fun startReceiving() {
    }

    override fun stopReceiving(abortCurrentTransfers: Boolean) {
    }

    override fun getPeers(): Set<Host> {
        return peers
    }

    override fun isReceiving(): Boolean {
        return serverState
    }
}