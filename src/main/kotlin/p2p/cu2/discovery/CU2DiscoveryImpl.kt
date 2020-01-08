package p2p.cu2.discovery

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import p2p.cu2.model.Host
import java.util.concurrent.TimeUnit

class CU2DiscoveryImpl(val discoverableTimeout: Long,
                       val discoveryTimeout: Long,
                       val discoverablePingInterval: Long,
                       val listener: CU2Discovery.Listener) : CU2Discovery {

    private var discoverable = false
    private var discovering = false
    private lateinit var discoverableDisposable: Disposable
    private lateinit var discoveryDisposable: Disposable
    private var currentPeers: MutableSet<Host> = mutableSetOf()
    private var broadcastService: UdpBroadcastService? = null
    private var serverService: UdpServerService? = null

    override fun makeDiscoverable(hostName: String) {
        if(!discoverable) {
            startBroadcast(hostName)
            discoverableDisposable = Observable.timer(discoverableTimeout, TimeUnit.MILLISECONDS, Schedulers.io())
                .subscribe {
                    if(discoverable) {
                        stopBroadcast()
                        listener.onDiscoverableTimeout()
                    }
                }
        }
    }

    override fun makeNonDiscoverable() {
        if(discoverable) {
            stopBroadcast()
            discoverableDisposable.dispose()
        }
    }

    private fun startBroadcast(hostName: String) {
        broadcastService = UdpBroadcastService()
        broadcastService!!.start(hostName, discoverablePingInterval)
        discoverable = true
    }

    private fun stopBroadcast() {
        broadcastService?.stop()
        discoverable = false
    }

    override fun startDiscovery() {
        if(!discovering) {
            startServer()
            discoveryDisposable = Observable.timer(discoveryTimeout, TimeUnit.MILLISECONDS, Schedulers.io())
                .subscribe{
                    if(discovering) {
                        stopDiscovery()
                        listener.onDiscoveryTimeout()
                    }
                }
        }
    }

    override fun stopDiscovery() {
        if(discovering) {
            stopServer()
            discoveryDisposable.dispose()
        }
    }

    private fun startServer() {
        serverService = UdpServerService()
        serverService!!.start(discoverablePingInterval * 2, object: UdpServerService.UdpBroadcastListener{
            override fun onServerSetupFailed(e: Throwable) {
                listener.onDiscoveryFailure(e)
            }

            override fun onReceiveFail() {
            }

            override fun onHostUpdate(currentHosts: Set<Host>) {
                currentPeers.retainAll(currentHosts)
                currentPeers.addAll(currentHosts)
                listener.onPeersUpdate(currentPeers)
            }

        })
        discovering = true
    }

    private fun stopServer() {
        serverService?.stop()
        discovering = false
    }

    override fun getAllAvaiablePeers(): Set<Host> {
        return currentPeers
    }

    override fun isDiscoverable(): Boolean {
        return discoverable
    }

    override fun isDiscovering(): Boolean {
        return discovering
    }

}