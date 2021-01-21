package com.example.partyremote.services

import android.content.Context
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

class SpotifyService {
    private val CLIENT_ID = "9128cfcc2523420e90fde0851968fdbc"
    private val REDIRECT_URI = "http://localhost:8888/callback"
    private var mSpotifyAppRemote: SpotifyAppRemote? = null

    fun connect(context: Context){
        // Set the connection parameters

        // Set the connection parameters
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(context, connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote
                    Log.d("MainActivity", "Connected! Yay!")

                    // Now you can start interacting with App Remote
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MainActivity", throwable.message, throwable)

                    // Something went wrong when attempting to connect! Handle errors here
                }
            })
    }

    fun playMusic() {
        mSpotifyAppRemote?.getPlayerApi()?.play("spotify:playlist:01DbkmjFPYPeZyw7MxBal5?si=cjqhTLRZR5WbvylOHNbalA");
    }

    fun pauseMusic() {
        mSpotifyAppRemote?.getPlayerApi()?.pause()
    }

    fun nextSong() {
        mSpotifyAppRemote?.getPlayerApi()?.skipNext()
    }

    fun previousSong() {
        mSpotifyAppRemote?.getPlayerApi()?.skipPrevious()
    }

    fun disconnect() {
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }
}