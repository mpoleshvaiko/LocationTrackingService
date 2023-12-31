package com.example.locationtrackingservice.stateMachine

sealed class States {
    object IDLE : States()
    object READY : States()
    object RUNNING : States()
    object DONE : States()
    object ERROR : States()
    companion object {
        fun values(): Array<States> {
            return arrayOf(IDLE, READY, RUNNING, DONE, ERROR)
        }

        fun valueOf(value: String): States {
            return when (value) {
                "IDLE" -> IDLE
                "READY" -> READY
                "RUNNING" -> RUNNING
                "DONE" -> DONE
                "ERROR" -> ERROR
                else -> throw IllegalArgumentException("No object com.example.locationtrackingservice.stateMachine.States.$value")
            }
        }
    }
}