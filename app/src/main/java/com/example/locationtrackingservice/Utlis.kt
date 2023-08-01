package com.example.locationtrackingservice

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(
    msg: String,
    length: Int,
    actionMessage: CharSequence,
    action: (View) -> Unit
) {
    Snackbar.make(this, msg, length).apply {
        setAction(actionMessage) { view ->
            action(view)
        }
        show()
    }
}