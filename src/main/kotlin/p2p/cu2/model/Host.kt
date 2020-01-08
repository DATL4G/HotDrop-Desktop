package p2p.cu2.model

import java.net.InetAddress

class Host(val inetAddress: InetAddress = InetAddress.getLoopbackAddress(),
           val name: String = InetAddress.getLocalHost().hostName) {

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(other !is Host) return false
        return inetAddress.hostAddress == other.inetAddress.hostAddress
    }

    override fun hashCode(): Int {
        return inetAddress.hashCode()
    }

    override fun toString(): String {
        return "Host{" +
                "address=" + inetAddress.hostAddress +
                ", name='" + name + '\'' +
                '}'
    }

}