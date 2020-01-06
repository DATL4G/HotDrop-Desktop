package fragments

import extends.AdvancedFragment
import javafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import utils.Device
import view.DeviceListItem
import java.net.InetAddress

class ChooseDeviceFragment(devices: Array<Device>) : AdvancedFragment() {
    override val root: StackPane by fxml("/fxml/choose_device_fragment.fxml", true)

    private val gridPane: GridPane by fxid()
    private var deviceClickListener: DeviceListItem.ClickListener
    var mListener: OnFragmentInteractionListener? = null

    init {
        applyMaxSize()
        applyZIndex()

        deviceClickListener = object: DeviceListItem.ClickListener{
            override fun onClick(item: Device) {
                mListener!!.onChooseFragmentInteraction(item)
            }
        }

        val computerName = InetAddress.getLocalHost().hostName

        val deviceArrayList = ArrayList<DeviceListItem>()
        devices.forEach {
            deviceArrayList.add(DeviceListItem(it, deviceClickListener))
        }

        var column = 0
        var row = 0

        for (item in deviceArrayList) {
            gridPane.add(item, column, row)

            if(column < 2) {
                column++
            } else {
                column = 0
                row++
            }
        }
    }

    interface OnFragmentInteractionListener {
        fun onChooseFragmentInteraction(device: Device)
    }
}