package com.github.kamil1338.recording_app.storage_use_case

import android.content.Context
import io.reactivex.Completable
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ExtractDatabaseUseCaseImpl(
    private val applicationContext: Context,
    private val recordingDatabase: RecordingDatabase,
    private val databaseName: Pair<String, String>
) : ExtractDatabaseUseCase {

    override fun execute(): Completable {
        return Completable.create { emitter ->
            try {
                val sourceName = generateSourceName()
                val destinationName = generateDestinationName()
                closeDatabase()
                copy(sourceName, destinationName)
                emitter.onComplete()
            } catch (exception: Exception) {
                emitter.onError(exception)
            }
        }
    }

    private fun generateSourceName() = "${databaseName.first}${databaseName.second}"

    private fun generateDestinationName(): String {
        val dateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        return "${databaseName.first}${dateFormat.format(Calendar.getInstance().time)}${databaseName.second}"
    }

    private fun closeDatabase() {
        recordingDatabase.close()
    }

    private fun copy(srcDatabaseName: String, dstDatabaseName: String) {
        var dstFile = applicationContext.getExternalFilesDir(null)

        if (dstFile != null && !dstFile.exists()) {
            dstFile.mkdirs()
        }

        dstFile = File(dstFile, dstDatabaseName)
        if (!dstFile.exists()) {
            dstFile.createNewFile()
        }

        val inputStream =
            FileInputStream(applicationContext.getDatabasePath(srcDatabaseName))
        val outputStream = FileOutputStream(dstFile)

        val buffer = ByteArray(4096)
        var length: Int = inputStream.read(buffer)
        while (length > 0) {
            outputStream.write(buffer, 0, length)
            length = inputStream.read(buffer)
        }
        outputStream.flush()
        inputStream.close()
        outputStream.close()
    }
}