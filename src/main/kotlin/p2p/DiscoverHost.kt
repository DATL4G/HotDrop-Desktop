package p2p

import MainView
import extends.AdvancedView
import fragments.ChooseDeviceFragment
import fragments.TransferFragment
import p2p.cu2.connect.CU2Connect
import p2p.cu2.discovery.CU2Discovery
import p2p.cu2.model.Host
import utils.DialogBuilder
import java.lang.Exception
import java.net.InetAddress

class DiscoverHost(private val view: AdvancedView) {

    private var cU2Discovery: CU2Discovery
    private var cU2Connect: CU2Connect
    private var inetAddress = InetAddress.getLoopbackAddress()

    init {
        cU2Discovery = CU2Discovery.Builder()
            .setDiscoverableTimeout(Long.MAX_VALUE)
            .setDiscoveryTimeout(Long.MAX_VALUE)
            .setDiscoverablePingInterval(750)
            .setDiscoveryListener(cu2DiscoveryListener)
            .build()

        cU2Connect = CU2Connect.Builder()
            .fromDiscovery(cU2Discovery)
            .setListener(cu2ConnectListener)
            .build()
    }

    fun startDiscovery() {
        if (!cU2Discovery.isDiscovering()) {
            cU2Discovery.makeDiscoverable("4${view.packageName}${InetAddress.getLocalHost().hostName}")
            if (!cU2Connect.isReceiving()) {
                cU2Connect.startReceiving()
            }
            cU2Discovery.startDiscovery()
        }
    }

    fun send(host: Host, bytes: ByteArray) {
        cU2Connect.send(bytes, host)
    }

    private val cu2DiscoveryListener: CU2Discovery.Listener
        get() = object : CU2Discovery.Listener {
            override fun onPeersUpdate(hosts: MutableSet<Host>) {
                for (host in hosts) {
                    if (!host.name.contains(view.packageName)) {
                        hosts.remove(host)
                    }
                    if (host.inetAddress.hostAddress == inetAddress.hostAddress) {
                        hosts.remove(host)
                    }
                }

                if (hosts.size > 0) {
                    if ((view as? MainView)?.chooseDeviceFragment == null) {
                        (view as? MainView)?.chooseDeviceFragment = ChooseDeviceFragment(hosts)
                    } else {
                        (view as? MainView)?.chooseDeviceFragment!!.setHosts(hosts)
                    }

                    (view as? MainView)?.hideFragment(view.searchFragment)
                    view.showFragment((view as? MainView)?.chooseDeviceFragment!!)


                } else {
                    (view as? MainView)?.chooseDeviceFragment?.hideFragment()
                    (view as? MainView)?.showFragment(view.searchFragment)

                    if (cU2Discovery.isDiscovering()) {
                        (view as? MainView)?.searchFragment?.setSearch(true)
                    } else {
                        (view as? MainView)?.searchFragment?.setSearch(false)
                    }
                }
            }

            override fun onDiscoveryTimeout() {
                stopDiscovery()
                if (view is MainView) {
                    view.searchFragment.setSearch(false)
                }
            }

            override fun onDiscoveryFailure(e: Throwable) {
                stopDiscovery()
                (view as? MainView)?.searchFragment?.setSearch(false)
            }

            override fun onDiscoverableTimeout() {
                stopDiscovery()
                (view as? MainView)?.searchFragment?.setSearch(false)
            }

        }

    private val cu2ConnectListener: CU2Connect.Listener
        get() = object : CU2Connect.Listener {
            override fun onReceive(bytes: ByteArray, sender: Host) {
                when (String(bytes)) {
                    MESSAGE_REQUEST_START_TRANSFER,
                    MESSAGE_RESPONSE_ACCEPT_REQUEST,
                    MESSAGE_RESPONSE_DECLINE_REQUEST -> startHostTransfer(sender, bytes)
                    else -> cU2Connect.send(MESSAGE_RESPONSE_DECLINE_REQUEST.toByteArray(), sender)
                }
            }

            override fun onSendComplete(jobId: Long) {}

            override fun onSendFailure(e: Throwable, jobId: Long) {}

            override fun onStartListenFailure(e: Throwable) {}

        }

    fun stopDiscovery() {
        if (cU2Discovery.isDiscovering()) {
            cU2Discovery.makeNonDiscoverable()
            cU2Discovery.stopDiscovery()
        }
        if (cU2Connect.isReceiving()) {
            cU2Connect.stopReceiving(false)
            (view as? MainView)?.searchFragment?.setSearch(false)
        }
    }

    private fun startHostTransfer(sender: Host, bytes: ByteArray) {
        val senderName = sender.name.substring(sender.name.indexOf(view.packageName) + view.packageName.length)
        when (String(bytes)) {
            MESSAGE_REQUEST_START_TRANSFER -> DialogBuilder(view.primaryStage.scene)
                .setMessage("$senderName wants to connect with you")
                .setPositiveButton("Start", "#db3236", object : DialogBuilder.OnClickListener {
                    override fun onClick() {
                        cU2Connect.send(MESSAGE_RESPONSE_ACCEPT_REQUEST.toByteArray(), sender)
                        stopDiscoveryAndStartTransfer(sender)
                    }
                }).setNegativeButton("Cancel", "#db3236", object : DialogBuilder.OnClickListener {
                    override fun onClick() {
                        cU2Connect.send(MESSAGE_RESPONSE_DECLINE_REQUEST.toByteArray(), sender)
                    }
                }).create()?.show()

            MESSAGE_RESPONSE_DECLINE_REQUEST -> DialogBuilder(view.primaryStage.scene)
                .setMessage("$senderName is busy at the moment")
                .setPositiveButton("Okay", "#db3236", null)
                .create()?.show()

            MESSAGE_RESPONSE_ACCEPT_REQUEST -> stopDiscoveryAndStartTransfer(sender)
            else -> cU2Connect.send(MESSAGE_RESPONSE_DECLINE_REQUEST.toByteArray(), sender)
        }
    }

    private fun stopDiscoveryAndStartTransfer(host: Host) {
        cU2Connect.stopReceiving(false)
        cU2Discovery.stopDiscovery()
        (view as? MainView)?.transferFragment = TransferFragment(host)
        (view as? MainView)?.chooseDeviceFragment?.hideFragment()
        (view as? MainView)?.searchFragment?.hideFragment()
        view.showFragment((view as? MainView)?.transferFragment!!)
    }

    companion object {
        const val MESSAGE_REQUEST_START_TRANSFER = "START_TRANSFER"
        const val MESSAGE_RESPONSE_DECLINE_REQUEST = "DECLINE_REQUEST"
        const val MESSAGE_RESPONSE_ACCEPT_REQUEST = "ACCEPT_REQUEST"
    }
}