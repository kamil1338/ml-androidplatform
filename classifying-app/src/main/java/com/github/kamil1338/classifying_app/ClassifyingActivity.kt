package com.github.kamil1338.classifying_app

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.github.kamil1338.recording_app.core_ui.BaseActivity

import kotlinx.android.synthetic.main.activity_classifying.*

class ClassifyingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classifying)
        setSupportActionBar(toolbar)
    }
}
