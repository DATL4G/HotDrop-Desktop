package utils

import com.jfoenix.animation.alert.JFXAlertAnimation
import com.jfoenix.controls.JFXAlert
import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXDialogLayout
import com.jfoenix.controls.JFXListView
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import javafx.scene.paint.Paint
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import tornadofx.add

class DialogBuilder internal constructor(scene: Scene) {

    var title: String? = null
    var message: String? = null
    var customView: Node? = null
    private var listView: JFXListView<Label>? = null
    var positiveButton: JFXButton? = null
    var negativeButton: JFXButton? = null
    private var window: Window? = null
    private var jfxDialogLayout: JFXDialogLayout? = null
    private var positiveButtonPaint: Paint? = null
    private var negativeButtonPaint: Paint? = null
    private var alert: JFXAlert<String>? = null

    init {
        window = scene.window
    }

    fun setTitle(title: String): DialogBuilder {
        this.title = title
        return this
    }

    fun setMessage(message: String): DialogBuilder {
        this.message = message
        return this
    }

    fun setView(node: Node): DialogBuilder {
        this.customView = node
        return this
    }

    fun setPositiveButton(buttonText: String, color: String? = null, listener: OnClickListener? = null): DialogBuilder {
        color?.let {
            positiveButtonPaint = Paint.valueOf(it)
        }

        positiveButton = JFXButton(buttonText.toUpperCase())
        positiveButton?.let {
            it.isDefaultButton = true
            it.isCancelButton = false
            it.textFill = positiveButtonPaint
            it.buttonType = JFXButton.ButtonType.FLAT
            it.setOnAction {
                alert?.hideWithAnimation()
                listener?.onClick()
            }
        }
        return this
    }

    fun setNegativeButton(buttonText: String, color: String?, listener: OnClickListener?): DialogBuilder {
        color?.let {
            negativeButtonPaint = Paint.valueOf(it)
        }

        negativeButton = JFXButton(buttonText.toUpperCase())
        negativeButton?.let {
            it.isDefaultButton = false
            it.isCancelButton = true
            it.textFill = negativeButtonPaint
            it.buttonType = JFXButton.ButtonType.FLAT
            it.setOnAction {
                alert?.hideWithAnimation()
                listener?.onClick()
            }
        }
        return this
    }

    fun setListItems(items: Array<String>, itemClickListener: ItemClickListener): DialogBuilder {
        listView = JFXListView()
        items.forEach {
            listView!!.items.add(Label(it))
        }

        listView?.let {
            it.setOnMouseClicked { _ -> itemClickListener.onSelect(it.selectionModel.selectedIndex) }
        }
        return this
    }

    fun create(): JFXAlert<String>? {
        alert = JFXAlert(window as Stage?)
        alert?.let {
            it.initModality(Modality.APPLICATION_MODAL)
            it.isOverlayClose = false
            it.animation = JFXAlertAnimation.BOTTOM_ANIMATION
        }

        jfxDialogLayout = JFXDialogLayout()
        jfxDialogLayout?.let {
            it.setHeading(Label(title))
            if(message != null && customView != null) {
                val vBox = VBox(Label(message))
                vBox.add(customView!!)
                it.setBody(vBox)
            } else if(customView != null) {
                it.setBody(customView)
            } else if(message != null && listView != null) {
                val vBox = VBox(Label(message))
                vBox.add(listView!!)
                it.setBody(vBox)
            } else if(listView != null) {
                it.setBody(listView)
            } else {
                it.setBody(VBox(Label(message)))
            }

            if(positiveButton != null && negativeButton != null) {
                it.setActions(negativeButton, positiveButton)
            } else {
                if(positiveButton != null) {
                    it.setActions(positiveButton)
                } else if (negativeButton != null) {
                    it.setActions(negativeButton)
                }
            }
        }

        alert?.setContent(jfxDialogLayout)
        return alert
    }

    interface OnClickListener{
        fun onClick()
    }

    interface ItemClickListener {
        fun onSelect(item: Int)
    }
}