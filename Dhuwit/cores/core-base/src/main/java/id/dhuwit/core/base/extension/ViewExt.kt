package id.dhuwit.core.base.extension

import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.enabled() {
    isEnabled = true
}

fun View.disabled() {
    isEnabled = false
}