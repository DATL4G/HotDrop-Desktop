package fragments

import extends.AdvancedFragment
import javafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import view.DeviceListItem

class ChooseDeviceFragment : AdvancedFragment() {
    override val root: StackPane by fxml("/fxml/choose_device_fragment.fxml", true)

    private val gridPane: GridPane by fxid()
    private var deviceArray: Array<DeviceListItem>
    private var deviceClickListener: DeviceListItem.ClickListener

    init {
        applyMaxSize()
        applyZIndex()

        deviceClickListener = object: DeviceListItem.ClickListener{
            override fun onClick(item: String) {
                println(item)
            }
        }

        deviceArray = arrayOf(DeviceListItem("Umidigi", deviceClickListener),
            DeviceListItem("Samsung", deviceClickListener),
            DeviceListItem("Huawei", deviceClickListener),
            DeviceListItem("Oneplus", deviceClickListener),
            DeviceListItem("Motorola", deviceClickListener))

        var column = 0
        var row = 0

        for (item in deviceArray) {
            gridPane.add(item, column, row)

            if(column < 2) {
                column++
            } else {
                column = 0
                row++
            }
        }
    }
}