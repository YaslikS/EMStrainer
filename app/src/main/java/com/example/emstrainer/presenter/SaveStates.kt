package com.example.emstrainer.presenter

class SaveStates {
    companion object {
        var power1 = 1
        var power2 = 1
        var power3 = 1
        var power4 = 1
        var power5 = 1
        var power6 = 1
        var power7 = 1
        var power8 = 1
        var power9 = 1
        var power10 = 1
        var power11 = 1
        var power12 = 1
        var byte225 = 1
        var byte208 = 1
        var radioId: Int = 0
        var switchFm: Boolean = false
        var switchAm: Boolean = false
        var changePower = 5
        var maxPower = 125
        var connectStatus: Boolean = false
        var nameSelectDevice: String = ""
        var addressSelectDevice: String = ""
        var batteryLevel = 0
        var LOG: String = ""
        var powerActivityMode: Boolean = true
        var position: Int = 0
        var fitLevel: Int = 0 // 0-normal | 1-easy | 2-medium | 3-hard
        var arMode: Boolean = true  // true - not Glass | false - with Glass
        var selectedArAnimation: Int = 0
        var selectedFit: Int = 0
        var fitMode: Boolean = true // true - one | false - full
    }
}