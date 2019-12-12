package com.github.kamil1338.recording_app.collecting_ui.collecting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.kamil1338.activity_recognition_core.core.ActivityType
import com.github.kamil1338.activity_recognition_core.core.SensorData
import com.github.kamil1338.activity_recognition_core.core.testing.ReactiveXUtils
import com.github.kamil1338.recording_app.collecting_use_case.NotificationUseCase
import com.github.kamil1338.recording_app.collecting_use_case.SensorUseCase
import com.github.kamil1338.recording_app.power_usecase.PowerUseCase
import com.github.kamil1338.recording_app.storage_use_case.AccelerometerDataDao
import com.github.kamil1338.recording_app.storage_use_case.SavingUseCase
import com.github.kamil1338.recording_app.storage_use_case.SavingUseCaseImpl
import com.github.kamil1338.recording_app.storage_use_case.entity.AccelerometerData
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class CollectingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    val testScheduler = TestScheduler()

    @get:Rule
    val rxRule = ReactiveXUtils().getTestRule(testScheduler)

    val notificationUseCase: NotificationUseCase = mock()
    val accelerometerDataDao: AccelerometerDataDao = mock()
    val sensorUseCase: SensorUseCase = mock()
    val powerUseCase: PowerUseCase = mock {
        on { execute() } doReturn Observable.just(Unit)
    }

    val savingUseCase: SavingUseCase =
        SavingUseCaseImpl(accelerometerDataDao)
    val tested = CollectingViewModel(
        savingUseCase,
        notificationUseCase,
        sensorUseCase,
        powerUseCase
    )

    @Test
    fun `given started collecting when started then sensors produce data`() {
        // given
        val data = generateListContainingData(5)
        val config = Config(
            durationInSec = 1,
            intervals = 1,
            activityType = ActivityType.Running,
            executionDelayInSec = 4
        )
        applyWheneverMockForSensorUseCase(data)

        // when
        tested.startCollectingData(config)

        // then
        testScheduler.advanceTimeBy(5L, TimeUnit.SECONDS)
        verify(sensorUseCase).execute()
    }

    @Test
    fun `given saving data to data base when executed then perform desired number of iterations`() {
        // given
        val config = Config(
            durationInSec = 20,
            intervals = 3,
            activityType = ActivityType.Running,
            executionDelayInSec = 0
        )
        val data = generateListContainingData(12)
        val intervalConfig = SensorConfig(5, TimeUnit.SECONDS)

        applyWheneverMockForSensorUseCase(data, intervalConfig)

        // when
        tested.startCollectingData(config)

        // then
        testScheduler.advanceTimeBy(intervalConfig.frequency, intervalConfig.timeUnit)
        testScheduler.advanceTimeBy(intervalConfig.frequency, intervalConfig.timeUnit)
        verifyZeroInteractions(accelerometerDataDao)
        testScheduler.advanceTimeBy(intervalConfig.frequency, intervalConfig.timeUnit)
        testScheduler.advanceTimeBy(intervalConfig.frequency, intervalConfig.timeUnit)
        verify(accelerometerDataDao, times(1)).insertAccelerometerData(any())
        testScheduler.advanceTimeBy(intervalConfig.frequency, intervalConfig.timeUnit)
        testScheduler.advanceTimeBy(intervalConfig.frequency, intervalConfig.timeUnit)
        verify(accelerometerDataDao, times(1)).insertAccelerometerData(any())
        testScheduler.advanceTimeBy(intervalConfig.frequency, intervalConfig.timeUnit)
        testScheduler.advanceTimeBy(intervalConfig.frequency, intervalConfig.timeUnit)
        verify(accelerometerDataDao, times(2)).insertAccelerometerData(any())
        testScheduler.advanceTimeBy(intervalConfig.frequency, intervalConfig.timeUnit)
        testScheduler.advanceTimeBy(intervalConfig.frequency, intervalConfig.timeUnit)
        verify(accelerometerDataDao, times(2)).insertAccelerometerData(any())
        testScheduler.advanceTimeBy(intervalConfig.frequency, intervalConfig.timeUnit)
        testScheduler.advanceTimeBy(intervalConfig.frequency, intervalConfig.timeUnit)
        verify(accelerometerDataDao, times(3)).insertAccelerometerData(any())
    }

    @Test
    fun `given notification when after duration then send starting collecting notification`() {
        // given
        val config = Config(
            durationInSec = 1,
            intervals = 1,
            activityType = ActivityType.Running,
            executionDelayInSec = 3
        )
        val data = generateListContainingData(1)
        val intervalConfig = SensorConfig(1, TimeUnit.SECONDS)

        applyWheneverMockForSensorUseCase(data, intervalConfig)

        // when
        tested.startCollectingData(config)

        // then
        testScheduler.advanceTimeBy(2, TimeUnit.SECONDS)
        verify(notificationUseCase, times(0)).sendStartCollectingNotification(any())
        testScheduler.advanceTimeBy(4, TimeUnit.SECONDS)
        verify(notificationUseCase, times(1)).sendStartCollectingNotification(any())
    }

    @Test
    fun `given notification when executed then send successful saving data to database notification`() {
        // given
        val config = Config(
            durationInSec = 30,
            intervals = 1,
            activityType = ActivityType.Running,
            executionDelayInSec = 5
        )
        val sizeOfData = 5
        val data = generateListContainingData(sizeOfData)
        val intervalConfig = SensorConfig(1, TimeUnit.SECONDS)

        applyWheneverMockForSensorUseCase(data, intervalConfig)

        // when
        tested.startCollectingData(config)

        // then
        testScheduler.advanceTimeBy(30, TimeUnit.SECONDS)
        verify(notificationUseCase).sendWriteToDbNotification(any(), eq(sizeOfData))
    }

    @Test
    fun `given collecting dao when use 2 intervals then receive correct number of data`() {
        // given
        val intervals = 3
        val probesPerOneInterval = 2
        val negativeEffectOfMockedSensorWrapper = 1
        val config = Config(
            durationInSec = probesPerOneInterval.toLong(),
            intervals = intervals.toLong(),
            activityType = ActivityType.Running,
            executionDelayInSec = 0
        )
        val sizeOfData = 100
        val data = generateListContainingData(sizeOfData)
        val intervalConfig = SensorConfig(1, TimeUnit.SECONDS)

        val dataToVerify =
            applyMockForAccelerometerDaoAndReturnListForVerification<AccelerometerData>(
                accelerometerDataDao
            )
        applyWheneverMockForSensorUseCase(data, intervalConfig)

        // when
        tested.startCollectingData(config)

        // then
        testScheduler.advanceTimeBy(30, TimeUnit.SECONDS)
        Assert.assertTrue(dataToVerify.size == (intervals * probesPerOneInterval - negativeEffectOfMockedSensorWrapper))
    }

    @Test
    fun `given 5 intervals when execute then invoke saving to databases logic 5 times`() {
        // given
        val intervals = 5
        val negativeEffectOfMockedSensorWrapper = 1
        val config = Config(
            durationInSec = 1,
            intervals = intervals.toLong(),
            activityType = ActivityType.Running,
            executionDelayInSec = 5
        )
        val data = generateListContainingData(99)
        val sensorConfig = SensorConfig(1, TimeUnit.SECONDS)

        val dataToVerify =
            applyMockForAccelerometerDaoAndReturnListForVerification<AccelerometerData>(
                accelerometerDataDao
            )
        applyWheneverMockForSensorUseCase(data, sensorConfig)

        // when
        tested.startCollectingData(config)

        // then
        testScheduler.advanceTimeBy(30, TimeUnit.SECONDS)
        verify(accelerometerDataDao, times(intervals)).insertAccelerometerData(any())
        Assert.assertTrue(dataToVerify.size == intervals - negativeEffectOfMockedSensorWrapper)
    }

    @Test
    fun `given work in progress notification when only first work starts then show work in progress notification`() {
        // given
        val config = Config(
            durationInSec = 1,
            intervals = 1,
            activityType = ActivityType.Running,
            executionDelayInSec = 0
        )
        val data = generateListContainingData(5)
        val sensorConfig = SensorConfig(1, TimeUnit.SECONDS)
        val tested = CollectingViewModel(
            savingUseCase,
            notificationUseCase,
            sensorUseCase,
            powerUseCase
        )

        applyWheneverMockForSensorUseCase(data, sensorConfig)

        // when
        tested.startCollectingData(config)

        // then
        testScheduler.advanceTimeBy(30, TimeUnit.SECONDS)
        verify(notificationUseCase, times(1)).sendWorkInProgressNotification()
    }

    @Test
    fun `given work in progress notification when starts another time then doesn't show work in progress notification`() {
        // given
        val config1 = Config(
            durationInSec = 20,
            intervals = 4,
            activityType = ActivityType.Running,
            executionDelayInSec = 0
        )
        val config2 = Config(
            durationInSec = 10,
            intervals = 1,
            activityType = ActivityType.Running,
            executionDelayInSec = 0
        )
        val data = generateListContainingData(5)
        val sensorConfig = SensorConfig(1, TimeUnit.SECONDS)
        val tested = CollectingViewModel(
            savingUseCase,
            notificationUseCase,
            sensorUseCase,
            powerUseCase
        )

        applyWheneverMockForSensorUseCase(data, sensorConfig)

        // when
        tested.startCollectingData(config1)
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        tested.startCollectingData(config2)

        // then
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        verify(notificationUseCase, times(1)).sendWorkInProgressNotification()
    }

    @Test
    fun `given work in progress notification when all collecting is finished then cancel work in progress notification`() {
        // given
        val config1 = Config(
            durationInSec = 10,
            intervals = 2,
            activityType = ActivityType.Running,
            executionDelayInSec = 0
        )
        val config2 = Config(
            durationInSec = 10,
            intervals = 4,
            activityType = ActivityType.Running,
            executionDelayInSec = 0
        )
        val data = generateListContainingData(5)
        val sensorConfig = SensorConfig(1, TimeUnit.SECONDS)

        val tested = CollectingViewModel(
            savingUseCase,
            notificationUseCase,
            sensorUseCase,
            powerUseCase
        )

        applyWheneverMockForSensorUseCase(data, sensorConfig)

        // when
        tested.startCollectingData(config1)
        testScheduler.advanceTimeBy(2, TimeUnit.SECONDS)
        tested.startCollectingData(config2)

        // then
        testScheduler.advanceTimeBy(30, TimeUnit.SECONDS)
        verify(notificationUseCase, times(1)).cancelBackgroundWorkNotification()
    }

    @Test
    fun `given work in progress notification when not all collecting is finished then doesn't cancel work in progress notification`() {
        // given
        val config1 = Config(
            durationInSec = 20,
            intervals = 4,
            activityType = ActivityType.Running,
            executionDelayInSec = 0
        )
        val config2 = Config(
            durationInSec = 10,
            intervals = 1,
            activityType = ActivityType.Running,
            executionDelayInSec = 0
        )
        val data = generateListContainingData(5)
        val sensorConfig = SensorConfig(1, TimeUnit.SECONDS)

        val tested = CollectingViewModel(
            savingUseCase,
            notificationUseCase,
            sensorUseCase,
            powerUseCase
        )

        applyWheneverMockForSensorUseCase(data, sensorConfig)

        // when
        tested.startCollectingData(config1)
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        tested.startCollectingData(config2)

        // then
        verify(notificationUseCase, times(0)).cancelBackgroundWorkNotification()
    }

    @Test
    fun `given execution delay when set then execute after delay`() {
        // given
        val executionDelayInSec = 6
        val config = Config(
            durationInSec = 20,
            intervals = 4,
            activityType = ActivityType.Running,
            executionDelayInSec = executionDelayInSec.toLong()
        )
        val data = generateListContainingData(1)
        val sensorConfig = SensorConfig(0, TimeUnit.SECONDS)

        applyWheneverMockForSensorUseCase(data, sensorConfig)

        // when
        tested.startCollectingData(config)

        // then
        verify(sensorUseCase, times(0)).execute()
        testScheduler.advanceTimeBy(executionDelayInSec.toLong(), TimeUnit.SECONDS)
        verify(sensorUseCase, times(1)).execute()
    }

    @Test
    fun `given use case when execute then completes successfully`() {
        // given
        val data = generateListContainingData(5)
        val config = Config(
            durationInSec = 1,
            intervals = 1,
            activityType = ActivityType.Running,
            executionDelayInSec = 0
        )
        applyWheneverMockForSensorUseCase(data)

        // when
        tested.startCollectingData(config)

        // then
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        verify(sensorUseCase).execute()
    }

    @Test
    fun `given observable when invoked then emits 2 events every one second`() {
        // given
        val data = generateListContainingData(5)
        val intervalConfig = SensorConfig(500, TimeUnit.MILLISECONDS)

        // when
        val observer = createObservableEmittingDataInIntervals(data, intervalConfig).test()

        // then
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer.assertValueCount(2)
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer.assertValueCount(4)
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer.assertValueCount(5)
    }

    @Test
    fun `given observable when invoked then emits 1 event every one second`() {
        // given
        val data = generateListContainingData(5)
        val intervalConfig = SensorConfig(1, TimeUnit.SECONDS)

        // when
        val observer = createObservableEmittingDataInIntervals(data, intervalConfig).test()

        // then
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer.assertValueCount(1)
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer.assertValueCount(2)
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer.assertValueCount(3)
    }

    @Test
    fun `given generated list with data when generated then its size is as expected`() {
        // given
        val requestedSizeOfData = 5

        // when
        val generatedData = generateListContainingData(requestedSizeOfData)

        // then
        assert(generatedData.size == requestedSizeOfData)
    }

    @Test
    fun `given power use case when starting collecting data then power use case is executed`() {
        // given
        val config = Config(
            durationInSec = 1,
            intervals = 1,
            activityType = ActivityType.Running,
            executionDelayInSec = 0
        )
        val data = generateListContainingData(5)
        val sensorConfig = SensorConfig(1, TimeUnit.SECONDS)
        val tested = CollectingViewModel(
            savingUseCase,
            notificationUseCase,
            sensorUseCase,
            powerUseCase
        )

        applyWheneverMockForSensorUseCase(data, sensorConfig)

        // when
        tested.startCollectingData(config)

        // then
        testScheduler.advanceTimeBy(30, TimeUnit.SECONDS)
        verify(powerUseCase).execute()
    }

    @Test
    fun `given 10 collecting when started collecting then collect 600 recordings`() {
        // given
        val config = Config(
            durationInSec = 2,
            intervals = 60,
            activityType = ActivityType.Running,
            executionDelayInSec = 0
        )
        val data = generateListContainingData(999)
        val sensorConfig = SensorConfig(500, TimeUnit.MILLISECONDS)

        applyWheneverMockForSensorUseCase(data, sensorConfig)

        // when
        for (i in 0 until 10) {
            tested.startCollectingData(config)
        }

        // then
        testScheduler.advanceTimeBy(5, TimeUnit.MINUTES)
        verify(accelerometerDataDao, times(600)).insertRecording(any())
    }

    fun <T> applyMockForAccelerometerDaoAndReturnListForVerification(
        accelerometerDataDao: AccelerometerDataDao
    ): List<T> {
        val dataToVerify = mutableListOf<T>()
        whenever(accelerometerDataDao.insertAccelerometerData(any())).then {
            (it.arguments.first() as Collection<Any>)
                .map { type ->
                    type as T
                }
                .forEach { accData ->
                    dataToVerify.add(accData)
                }
        }
        return dataToVerify
    }

    fun applyWheneverMockForSensorUseCase(
        data: List<SensorData>,
        sensorConfig: SensorConfig? = null
    ) {
        if (sensorConfig != null) {
            whenever(sensorUseCase.execute())
                .doReturn(createObservableEmittingDataInIntervals(data, sensorConfig))
        } else {
            whenever(sensorUseCase.execute())
                .doReturn(createObservableEmittingDataInIntervals(data))
        }
    }

    private fun generateListContainingData(sizeOfGeneratedData: Int = 5) =
        (0 until sizeOfGeneratedData).map {
            SensorData(
                it.toFloat(),
                it.toFloat(),
                it.toFloat()
            )
        }.toList()

    private fun createObservableEmittingDataInIntervals(
        data: List<SensorData>,
        intervalConfig: SensorConfig = SensorConfig(400, TimeUnit.MILLISECONDS)
    ) = Observable.zip(
        Observable.fromIterable(data),
        Observable.interval(intervalConfig.frequency, intervalConfig.timeUnit),
        BiFunction<SensorData, Long, SensorData> { p0, _ -> p0 }
    )

    class SensorConfig(val frequency: Long, val timeUnit: TimeUnit)
}