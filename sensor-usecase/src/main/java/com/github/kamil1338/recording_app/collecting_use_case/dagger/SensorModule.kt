package com.github.kamil1338.recording_app.collecting_use_case.dagger

import dagger.Module

@Module(includes = [SensorProviderModule::class])
abstract class SensorModule {

}