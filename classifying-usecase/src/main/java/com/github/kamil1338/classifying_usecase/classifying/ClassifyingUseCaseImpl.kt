package com.github.kamil1338.classifying_usecase.classifying

import android.content.res.AssetManager
import com.github.kamil1338.activity_recognition_core.core.ActivityType
import com.github.kamil1338.classifying_usecase.classifying.ClassifyingUseCase.Companion.BYTE_SIZE_OF_FLOAT
import com.github.kamil1338.classifying_usecase.classifying.ClassifyingUseCase.Companion.INPUT_BATCH_SIZE
import com.github.kamil1338.classifying_usecase.classifying.ClassifyingUseCase.Companion.OUTPUT_LENGTH
import io.reactivex.Observable
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.min

class ClassifyingUseCaseImpl(
    private val assetManager: AssetManager,
    private val resultCalculator: ResultCalculator,
    modelXPath: String,
    modelYPath: String,
    modelZPath: String,
    labelsPath: String
) : ClassifyingUseCase {

    private val tfLiteBufferX: MappedByteBuffer = loadModelFile(modelXPath)
    private val tfLiteBufferY: MappedByteBuffer = loadModelFile(modelYPath)
    private val tfLiteBufferZ: MappedByteBuffer = loadModelFile(modelZPath)

    private val tfLiteOptions = Interpreter.Options().apply { setNumThreads(4) }

    private val modelXInterpreter: Interpreter
    private val modelYInterpreter: Interpreter
    private val modelZInterpreter: Interpreter

    private val labels = loadLabels(labelsPath)

    init {
        modelXInterpreter = Interpreter(tfLiteBufferX, tfLiteOptions)
        modelYInterpreter = Interpreter(tfLiteBufferY, tfLiteOptions)
        modelZInterpreter = Interpreter(tfLiteBufferZ, tfLiteOptions)
    }

    override fun execute(data: List<ClassificationData>): Observable<ActivityType> {
        return Observable.fromCallable {
            val xEntry = getResult(data.map { it.x }, modelXInterpreter)
            val yEntry = getResult(data.map { it.y }, modelYInterpreter)
            val zEntry = getResult(data.map { it.z }, modelZInterpreter)

            val winnerIndex = resultCalculator.calculateWinner(xEntry, yEntry, zEntry)
            return@fromCallable getActivityForIndex(winnerIndex)
        }
    }

    private fun loadModelFile(path: String): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(path)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel

        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        val buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)

        fileChannel.close()
        inputStream.close()
        fileDescriptor.close()

        return buffer
    }

    private fun loadLabels(path: String): List<String> {
        val inputStream = assetManager.open(path)
        val inputStreamReader = InputStreamReader(inputStream)
        val reader = BufferedReader(inputStreamReader)

        val labels = reader.readLines()

        reader.close()
        inputStream.close()

        return labels
    }

    private fun classifyActivity(interpreter: Interpreter, data: List<Float>): Array<FloatArray> {
        val inputBuffer = ByteBuffer.allocateDirect(INPUT_BATCH_SIZE * BYTE_SIZE_OF_FLOAT)
        inputBuffer.order(ByteOrder.nativeOrder())
        val end = min(INPUT_BATCH_SIZE, data.size)
        (0 until end).forEach { i ->
            inputBuffer.putFloat(data[i])
        }

        val outputBuffer = Array(1) { FloatArray(OUTPUT_LENGTH) { 0f } }
        interpreter.run(inputBuffer, outputBuffer)
        return outputBuffer
    }

    private fun getResult(data: List<Float>, interpreter: Interpreter): Map.Entry<Int, Float> {
        val outputBuffer = classifyActivity(interpreter, data)
        return resultCalculator.getMostProbableIndexAndItsValue(labels, outputBuffer)
    }

    private fun getActivityForIndex(index: Int): ActivityType {
        val label = labels[index]
        return ActivityType.values().first { it.name == label }
    }
}