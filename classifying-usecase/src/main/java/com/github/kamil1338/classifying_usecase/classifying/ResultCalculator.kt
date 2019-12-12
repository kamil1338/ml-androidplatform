package com.github.kamil1338.classifying_usecase.classifying

import com.github.kamil1338.classifying_usecase.classifying.ClassifyingUseCase.Companion.RESULTS_TO_SHOW
import java.util.*
import kotlin.Comparator

typealias MapEntry = Map.Entry<Int, Float>

class ResultCalculator {

    fun getMostProbableIndexAndItsValue(
        labels: List<String>,
        outputBuffer: Array<FloatArray>
    ): Map.Entry<Int, Float> {
        val sortedLabels = PriorityQueue<Map.Entry<Int, Float>>(RESULTS_TO_SHOW) { e1, e2 ->
            e1.value.compareTo(e2.value)
        }
        (labels.indices).forEach { i ->
            sortedLabels.add(object : MapEntry {
                override val key = i
                override val value = outputBuffer[0][i]
            })
            if (sortedLabels.size > RESULTS_TO_SHOW) {
                sortedLabels.poll()
            }
        }

        return sortedLabels.poll()
    }

    fun calculateWinner(
        xEntry: Map.Entry<Int, Float>,
        yEntry: Map.Entry<Int, Float>,
        zEntry: Map.Entry<Int, Float>
    ): Int {
        val classes = countedClasses(xEntry, yEntry, zEntry)
        return if (isUnclearCase(classes)) {
            getWinnerForComparator(
                xEntry,
                yEntry,
                zEntry,
                Comparator { e1, e2 -> e2.value.compareTo(e1.value) })
        } else {
            getWinnerForComparator(
                xEntry,
                yEntry,
                zEntry,
                Comparator { e1, e2 -> e1.key.compareTo(e2.key) })
        }
    }

    fun countedClasses(
        xEntry: Map.Entry<Int, Float>,
        yEntry: Map.Entry<Int, Float>,
        zEntry: Map.Entry<Int, Float>
    ): List<Int> {
        val result = arrayListOf(0, 0, 0)
        result[xEntry.key]++
        result[yEntry.key]++
        result[zEntry.key]++
        return result
    }

    fun isUnclearCase(list: List<Int>): Boolean {
        list.indices.forEach { i ->
            list.indices.forEach { j ->
                if (i != j && list[i] == list[j]) {
                    return true
                }
            }
        }
        return false
    }

    fun getWinnerForComparator(
        xEntry: Map.Entry<Int, Float>,
        yEntry: Map.Entry<Int, Float>,
        zEntry: Map.Entry<Int, Float>,
        comparator: Comparator<Map.Entry<Int, Float>>
    ): Int =
        listOf(xEntry, yEntry, zEntry)
            .sortedWith(comparator)
            .first()
            .key

    fun getWinnerProbability(
        xEntry: Map.Entry<Int, Float>,
        yEntry: Map.Entry<Int, Float>,
        zEntry: Map.Entry<Int, Float>
    ): Int =
        listOf(xEntry, yEntry, zEntry)
            .sortedWith(Comparator { e1, e2 -> e2.value.compareTo(e1.value) })
            .first()
            .key


    fun getWinnerClass(
        xEntry: Map.Entry<Int, Float>,
        yEntry: Map.Entry<Int, Float>,
        zEntry: Map.Entry<Int, Float>
    ): Int = listOf(xEntry, yEntry, zEntry)
        .sortedWith(Comparator { e1, e2 -> e2.key.compareTo(e1.key) })
        .first()
        .key
}