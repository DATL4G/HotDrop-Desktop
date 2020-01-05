package view

import com.jfoenix.controls.JFXButton

class FAB() : JFXButton() {

    init {
        buttonType = ButtonType.RAISED

        maxHeight = Double.NEGATIVE_INFINITY
        minHeight = Double.NEGATIVE_INFINITY

        maxWidth = Double.NEGATIVE_INFINITY
        minWidth = Double.NEGATIVE_INFINITY

        prefHeight = 50.0
        prefWidth = 50.0

        text = null
    }
}