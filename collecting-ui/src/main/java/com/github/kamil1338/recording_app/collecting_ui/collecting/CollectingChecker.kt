package com.github.kamil1338.recording_app.collecting_ui.collecting

class CollectingChecker(
    private var startedCollections: Int = 0,
    private var finishedCollections: Int = 0
) {

    fun notifyNextFinish() {
        synchronized(this) {
            ++finishedCollections
        }
    }

    fun notifyNextCollecting() {
        synchronized(this) {
            ++startedCollections
        }
    }

    val workNotStartedYet: Boolean
        get() {
            synchronized(this) {
                return startedCollections == 0
            }
        }

    val isAllWorkDone: Boolean
        get() {
            synchronized(this) {
                return startedCollections == finishedCollections
            }
        }

    fun reset() {
        synchronized(this) {
            startedCollections = 0
            finishedCollections = 0
        }
    }
}