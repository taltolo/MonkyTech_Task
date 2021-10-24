package com.parallelcodes.monkeytech.Utils

import android.content.Context

import android.media.MediaPlayer

import android.os.AsyncTask


class BackgroundSound(context: Context) : AsyncTask<Void, Void, Context?>() {
    var context =context
     override fun doInBackground(vararg params: Void?): Context? {

        val player: MediaPlayer = MediaPlayer.create(context,
            com.parallelcodes.monkeytech.R.raw.spiderpigall
        )
        player.isLooping = false // Set looping
        player.setVolume(1.0f, 1.0f)
        player.start()

         while (!isCancelled) {
         }

         player.stop()
         player.release()

         return null
    }
}