package utils

class Device(val hostName: String) {

    var name: String
    private var iconNumber: Char
    var iconName: String

    init {
        iconNumber = hostName[0]
        name = hostName.substring(hostName.indexOf(packageName) + packageName.length)
        when(iconNumber) {
            '1' -> iconName = materialPrefix+"phone-android"
            '2' -> iconName = materialPrefix+"smartphone"
            '3' -> iconName = materialPrefix+"tablet-android"
            '4' -> iconName = materialPrefix+"tv"
            else -> iconName = materialPrefix+"watch"
        }
    }

    companion object {
        const val packageName = "de.datlag.hotdrop"
        const val materialPrefix = "gmi-"
    }
}