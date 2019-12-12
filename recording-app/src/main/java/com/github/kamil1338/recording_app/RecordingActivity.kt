package com.github.kamil1338.recording_app

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.github.kamil1338.recording_app.core_ui.BaseActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class RecordingActivity : BaseActivity() {

    @Inject
    lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        homeViewModel.permissionGranted.observe(this,
            Observer { handlePermissionCheckingResult(it) })
        homeViewModel.extractingSucceeded.observe(this,
            Observer { handleExtractionResult(it) })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_extract -> {
                homeViewModel.checkWritePermission()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handlePermissionCheckingResult(granted: Boolean?) {
        when (granted) {
            true -> performOperationUsingGrantedPermission()
            false -> requestPermission()
        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(homeViewModel.requestedPermission), PERMISSION_REQUEST_CODE)
    }

    private fun performOperationUsingGrantedPermission() {
        homeViewModel.extractDatabase()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (isCorrectRequestCode(requestCode)) {
            if (isPermissionGranted(grantResults)) {
                performOperationUsingGrantedPermission()
            }
        }
    }

    private fun isCorrectRequestCode(requestCode: Int) = requestCode == PERMISSION_REQUEST_CODE

    private fun isPermissionGranted(grantResults: IntArray) =
        (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)

    private fun handleExtractionResult(result: Boolean?) {
        when (result) {
            true -> showInfoAboutSuccessfulExtraction()
            false -> showInfoAboutFailedExtraction()
        }
    }

    private fun showInfoAboutSuccessfulExtraction() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "Database extracted",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showInfoAboutFailedExtraction() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "Database extraction failed",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 1999
    }
}
