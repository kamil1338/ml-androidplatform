package com.github.kamil1338.recording_app.collecting_ui

import android.widget.SeekBar
import androidx.databinding.BindingAdapter


object BindingAdapters {

    @BindingAdapter("app:onProgressChanged")
    @JvmStatic
    fun setListener(view: SeekBar, listener: SeekBar.OnSeekBarChangeListener) {
        view.setOnSeekBarChangeListener(listener)
    }
}