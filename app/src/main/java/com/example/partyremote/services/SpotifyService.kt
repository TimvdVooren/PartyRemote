package com.example.partyremote.services

import android.content.Context
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

class SpotifyService {
    private val CLIENT_ID = "141360a1a2f84ddcad0e23cb2d67ad48"
    private val REDIRECT_URI = "http://localhost:8888/callback"
    private var spotifyAppRemote: SpotifyAppRemote? = null
    private var isPlaylistPlaying = false

    fun connect(context: Context){
        // Set the connection parameters
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(context, connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    this@SpotifyService.spotifyAppRemote = spotifyAppRemote
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
        if (!isPlaylistPlaying) {
            spotifyAppRemote?.playerApi?.play("spotify:playlist:6wqk4X53DTXIjOAoFDIj40")
            isPlaylistPlaying = true
        } else {
            spotifyAppRemote?.playerApi?.resume()
        }
    }

    fun pauseMusic() {
        spotifyAppRemote?.playerApi?.pause()
    }

    fun nextSong() {
        spotifyAppRemote?.playerApi?.skipNext()
    }

    fun previousSong() {
        spotifyAppRemote?.playerApi?.skipPrevious()
    }

    fun disconnect() {
        SpotifyAppRemote.disconnect(spotifyAppRemote);
    }
}