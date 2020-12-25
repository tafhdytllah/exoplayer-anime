package com.tafh.exoplayeranime

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionUtil
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var player: SimpleExoPlayer
    lateinit var playerView: PlayerView
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerView = findViewById(R.id.playerView)
        progressBar = findViewById(R.id.progressBar)

        initPlayer()

        loadVod("https://s3usercontent.gomuni.me/videos/EfZaCgRJ1pDyz8nhIHAxvg/1608858000/4aa4065f94e95b6ccaa929d0f9cedd8f.mp4")


    }

    fun initPlayer() {

        val adaptiveTrackSelection = AdaptiveTrackSelection.Factory()
        player = ExoPlayerFactory.newSimpleInstance(
            this,
            DefaultRenderersFactory(this),
            DefaultTrackSelector(adaptiveTrackSelection),
            DefaultLoadControl())

        playerView.player = player
        player.addListener(object : Player.EventListener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when(playbackState){
                    ExoPlayer.STATE_BUFFERING -> progressBar.visibility = View.VISIBLE
                    ExoPlayer.STATE_READY -> progressBar.visibility = View.GONE
                }
            }
        })
    }

    fun loadVod(url: String) {
        val dataSourceFactory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, "Exo"),
            DefaultBandwidthMeter()
        )
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url))
        player.prepare(mediaSource)
        player.playWhenReady = true

    }

}