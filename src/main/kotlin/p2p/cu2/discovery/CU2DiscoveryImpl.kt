package p2p.cu2.discovery

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import p2p.cu2.model.Host
import java.util.concurrent.TimeUnit

class CU2DiscoveryImpl(val discoverableTimeout: Long,
                       val discoveryTimeout: Long,
                       val discoverablePingInterval: Long,
                       val listener: CU2Discovery.Listener,
                       port: Int,
                       regex: Regex) : CU2Discovery {
    override var isDiscoverable: Boolean = false
        private set
    override var isDiscovering: Boolean = false
        private set

    private lateinit var mDiscoverableDisposable: Disposable
    private lateinit var mDiscoveryDisposable: Disposable
    private val mCurrentPeers: MutableSet<Host> = mutableSetOf()

    override fun makeDiscoverable(hostName: String, mustMatch: String) {
        if(!isDiscoverable) {
            val hostJson = JSONObject(mapOf(Host.JSON_NAME to hostName, Host.JSON_FILTER_TEXT to mustMatch))
            beDiscoverable(hostJson)
            mDiscoverableDisposable = Observable.timer(discoverableTimeout, TimeUnit.MILLISECONDS, Schedulers.io())
                .subscribe {
                    if (isDiscoverable) {
                        stopBeingDiscoverable()
                        val thread = Thread{ listener.onDiscoverableTimeout() }
                        thread.start()
                        thread.join()
                    }
                }
        }
    }

    override fun makeNonDiscoverable() {
        if (isDiscoverable) {
            stopBeingDiscoverable()
            mDiscoverableDisposable.dispose()
        }
    }

    private fun beDiscoverable(hostJson: JSONObject) {
        TODO("start client")
        isDiscoverable = true
    }

    private fun stopBeingDiscoverable() {
        TODO("stop client")
        isDiscoverable = false
    }

    override fun startDiscovery() {
        if (!isDiscovering) {
            startServer()
            isDiscovering = true
            mDiscoveryDisposable = Observable.timer(discoveryTimeout, TimeUnit.MILLISECONDS, Schedulers.io())
                .subscribe {
                    if (isDiscovering) {
                        stopDiscovery()
                        val thread = Thread{ listener.onDiscoveryTimeout() }
                        thread.start()
                        thread.join()
                    }
                }
        }
    }

    private fun startServer() {
        TODO("start server")
    }

    override fun stopDiscovery() {
        if (isDiscovering) {
            TODO("stop server")
            isDiscovering = false
            mDiscoveryDisposable.dispose()
        }
    }

    override val allAvailablePeers: Set<Host> = mCurrentPeers


}