import com.jfoenix.controls.JFXButton
import extends.AdvancedView
import fragments.ChooseDeviceFragment
import fragments.InfoFragment
import fragments.SearchFragment
import fragments.TransferFragment
import javafx.fxml.FXML
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import utils.Device
import utils.DialogBuilder
import java.net.InetAddress


class MainView : AdvancedView("HotDrop"), ChooseDeviceFragment.OnFragmentInteractionListener {
    override val root : BorderPane by fxml("/fxml/main.fxml", true)
    override val fragmentPane: AnchorPane by fxid("centerPane")

    private val infoIcon: JFXButton by fxid()

    private val infoFragment = InfoFragment()
    private val searchFragment = SearchFragment()
    private var chooseDeviceFragment: ChooseDeviceFragment
    private lateinit var transferFragment: TransferFragment
    private var settingsArray: Array<String>

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


    fun infoLayout() {
        showFragment(infoFragment)
    }

    init {
        showFragment(searchFragment)
        infoIcon.setOnMouseClicked { infoLayout() }

        settingsArray = arrayOf("Account", "Connectivity", "Donate")


        val computerName = InetAddress.getLocalHost().hostName
        val deviceArray = arrayOf(Device("1de.datlag.hotdropUmidigi"),
            Device("2de.datlag.hotdrop$computerName"),
            Device("3de.datlag.hotdropHuawei"),
            Device("4de.datlag.hotdropOneplus"),
            Device("5de.datlag.hotdrop$computerName"))

        chooseDeviceFragment = ChooseDeviceFragment(deviceArray)
        chooseDeviceFragment.mListener = this
    }

    override fun onChooseFragmentInteraction(device: Device) {
        transferFragment = TransferFragment(device)
        DialogBuilder(primaryStage.scene)
            .setTitle(device.name)
            .setMessage("Connect to this device?")
            .setPositiveButton("Request", "#db3236", object: DialogBuilder.OnClickListener{
                override fun onClick() {
                    hideFragment(chooseDeviceFragment)
                    showFragment(transferFragment)
                }
            }).create()?.show()
    }
}

