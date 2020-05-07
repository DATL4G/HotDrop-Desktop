package p2p.cu2.connect

import java.io.DataInputStream
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket


class TcpServerService {

    companion object {
        const val SERVER_PORT = 6789
    }

    private var started = false
    private var serverSocket: ServerSocket? = null

    fun startServer(listener: TcpServerListener) {
        started = true
        try {
            serverSocket = ServerSocket()
            serverSocket!!.reuseAddress = true
            serverSocket!!.soTimeout = 0
            serverSocket!!.bind(InetSocketAddress(SERVER_PORT))

            while (started) {
                try {
                    val connectionSocket = serverSocket!!.accept()
                    onNewReceive(connectionSocket, listener)
                } catch (e: IOException) { }
            }
        } catch (e: IOException) {
            listener.onStartFailure(e)
        } finally {
            if(serverSocket != null) {
                if(!serverSocket!!.isClosed) {
                    try {
                        serverSocket!!.close()
                    } catch (e: IOException) { }
                }
            }
        }
    }

    private fun onNewReceive(connectionSocket: Socket,
                             listener: TcpServerListener) {
        var bytes: ByteArray? = null
        try {
            val dataInputStream = DataInputStream(connectionSocket.getInputStream())

            val length = dataInputStream.readInt()
            if(length > 0) {
                bytes = ByteArray(length)
                dataInputStream.readFully(bytes, 0, bytes.size)
            }
            onReceive(listener, bytes, connectionSocket.inetAddress)
        } catch (e: IOException) { }
    }

    private fun onReceive(listener: TcpServerListener,
                          bytes: ByteArray?,
                          inetAddress: InetAddress) {
        listener.onReceive(bytes, inetAddress)
    }

    fun stopServer() {
        started = false
        try {
            serverSocket?.close()
        } catch (e: IOException) { }
    }

    abstract class TcpServerListener {
        fun onStartFailure(e: Throwable) {
            onServerStartFailed(e)
        }

        abstract fun onServerStartFailed(e: Throwable)
        abstract fun onReceive(bytes: ByteArray?, inetAddress: InetAddress)
    }
}