package p2p.cu2.connect

import p2p.cu2.model.Host
import java.io.DataOutputStream
import java.io.IOException
import java.net.InetAddress
import java.net.Socket

class TcpClientService(private val listener: Listener) {

    fun send(data: ByteArray, destination: Host, jobId: Long) {
        startSending(data, destination, jobId)
    }

    private fun startSending(data: ByteArray,
                     destination: Host,
                     jobId: Long) {

        val destAdress: InetAddress
        var socket: Socket? = null


        try {
            destAdress = InetAddress.getByName(destination.inetAddress.hostAddress)
            socket = Socket(destAdress, TcpServerService.SERVER_PORT)
            val dOut = DataOutputStream(socket.getOutputStream())

            dOut.writeInt(data.size)
            dOut.write(data)

            listener.onSendSuccess(jobId)
        } catch (e: IOException) {
            listener.onSendFailure(jobId, e)
        } finally {
            socket?.let {
                try {
                    it.close()
                } catch (e: IOException) { }
            }
        }
    }

    interface Listener {
        fun onSendSuccess(jobId: Long)
        fun onSendFailure(jobId: Long, e: Throwable)
    }
}