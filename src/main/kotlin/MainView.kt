import extends.AdvancedView
import fragments.ChooseDeviceFragment
import fragments.InfoFragment
import fragments.SearchFragment
import fragments.TransferFragment
import javafx.fxml.FXML
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import p2p.DiscoverHost
import p2p.cu2.discovery.CU2Discovery
import p2p.cu2.model.Host
import utils.Device
import utils.DialogBuilder
import java.net.InetAddress


class MainView : AdvancedView("HotDrop"), ChooseDeviceFragment.OnFragmentInteractionListener {
    override val root : BorderPane by fxml("/fxml/main.fxml", true)
    override val fragmentPane: AnchorPane by fxid()
    private val centerPane: AnchorPane by fxid()

    private val infoFragment = InfoFragment()
    var searchFragment: SearchFragment
    var chooseDeviceFragment: ChooseDeviceFragment? = null
    var transferFragment: TransferFragment? = null
    private var settingsArray: Array<String>
    private var discoverHost = DiscoverHost(this@MainView)

    @FXML
    fun settingsDialog() {
        DialogBuilder(primaryStage.scene)
            .setTitle("Settings")
            .setListItems(settingsArray, object: DialogBuilder.ItemClickListener{
                override fun onSelect(item: Int) {
                    when(item) {
                        2 -> openURL("https://paypal.me/datlag")
                    }
                }
            })
            .setPositiveButton("Close", "#db3236")
            .create()?.show()
    }

    @FXML
    fun infoLayout() {
        showFragment(infoFragment, centerPane)
    }

    init {
        searchFragment = SearchFragment.newInstance(discoverHost)
        showFragment(searchFragment)
        settingsArray = arrayOf("Account", "Connectivity", "Donate")
    }

    override fun onDelete() {
        super.onDelete()
        discoverHost.stopDiscovery()
    }

    override fun onChooseFragmentInteraction(host: Host) {
        discoverHost.send(host, DiscoverHost.MESSAGE_REQUEST_START_TRANSFER.toByteArray())
    }
}

