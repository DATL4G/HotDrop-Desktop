package p2p.cu2.model

import org.json.JSONObject
import java.net.InetAddress

class Host {

    private val inetAddress: InetAddress
    val name: String
    val filterText: String

    private constructor() {
        inetAddress = LOOPBACK_ADDRESS
        name = DUMMY
        filterText = FILTER_TEXT
    }

    constructor(address: InetAddress, name: String, filterText: String) {
        inetAddress = address
        this.name = name
        this.filterText = filterText
    }

    val hostAddress: String
        get() = inetAddress.hostAddress

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Host) return false
        return inetAddress.hostAddress == other.inetAddress.hostAddress
    }

    override fun hashCode(): Int {
        return inetAddress.hashCode()
    }

    override fun toString(): String {
        val hostMap: Map<String, Any> = mapOf("address" to inetAddress.hostAddress,
                JSON_NAME to name,
                JSON_FILTER_TEXT to filterText)
        return JSONObject(hostMap).toString()
    }

    companion object {
        const val DUMMY = "dummy"
        val LOOPBACK_ADDRESS: InetAddress = InetAddress.getLoopbackAddress()
        const val FILTER_TEXT = ""
        const val JSON_NAME = "name"
        const val JSON_FILTER_TEXT = "filterText"
    }
}