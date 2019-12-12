package com.github.kamil1338.classifying_usecase.sound

import android.content.Context
import android.media.MediaPlayer
import com.example.classifying_usecase.R

class SoundUseCaseImpl(applicationContext: Context) : SoundUseCase {

    private val otherSoundPlayer: MediaPlayer =
        MediaPlayer.create(applicationContext, R.raw.other)
    private val walkingSoundPlayer: MediaPlayer =
        MediaPlayer.create(applicationContext, R.raw.walking)
    private val runningSoundPlayer: MediaPlayer =
        MediaPlayer.create(applicationContext, R.raw.running)

    override fun playSoundOther() {
        otherSoundPlayer.start()
    }

    override fun playSoundWalking() {
        walkingSoundPlayer.start()
    }

    override fun playSoundRunning() {
        runningSoundPlayer.start()
    }
}