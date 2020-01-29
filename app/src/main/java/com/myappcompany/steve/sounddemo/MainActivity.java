package com.myappcompany.steve.sounddemo;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.floor;
import static java.lang.Math.max;
import static java.lang.Math.round;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private int songLengthMilli;
    private TextView currentTimeStamp;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentTimeStamp = findViewById(R.id.textCurrentTimeStamp);

        TextView endTimeStamp = findViewById(R.id.textEndTimeStamp);

        mediaPlayer = MediaPlayer.create(this, R.raw.marbles);

        songLengthMilli = mediaPlayer.getDuration();
        int endTimeMin = Math.floorDiv(songLengthMilli, 1000*60);
        int endTimeSec = Math.floorDiv(songLengthMilli - endTimeMin*60*1000, 1000);
        String endTimeMinString = (endTimeMin < 10 ? "0" + String.valueOf(endTimeMin) : String.valueOf(endTimeMin));
        String endTimeSecString = (endTimeSec < 10 ? "0" + String.valueOf(endTimeSec) : String.valueOf(endTimeSec));

        endTimeStamp.setText(endTimeMinString + ":" + endTimeSecString);


        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(songLengthMilli);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("progress as int", String.valueOf(progress));
                mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();
            }
        });

        //Code for the volume control seekbar which requires an AudioManager
        //first initialize the audio manager using getSystemService(AUDIO_SERVICE)
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        //Grab the volumeControl seekbar
        SeekBar volumeControl = findViewById(R.id.volumeSeekBar);
        //Set the maxVolume to the STREAM_MUSIC max audio defined by AudioManager
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //Change the default SeekBar max from 100 to maxVolume
        volumeControl.setMax(maxVolume);
        //initialize the slider to the maxVolume
        volumeControl.setProgress(maxVolume);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
        //create the SeekBar listener that handles changes to the volumeControl SeekBar
        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("Volume SeekBar changed", String.valueOf(progress));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Checks periodically to see whether the timer needs to be updated.
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //currentTimeStamp.setText(updateTimeStamp());
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 1000);

        //updateTimeStamp();
    }

    public void Pause(View view) {

        this.mediaPlayer.pause();
    }

    public void Play(View view) {
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }
        }

    public void Rewind(View view) {
        this.mediaPlayer.pause();
        this.mediaPlayer.seekTo(Math.min(Math.max(round(mediaPlayer.getCurrentPosition())-1000, 0),mediaPlayer.getDuration()));
        this.mediaPlayer.start();
    }

    public void Forward(View view) {
        this.mediaPlayer.pause();
        this.mediaPlayer.seekTo(Math.min(Math.max(round(mediaPlayer.getCurrentPosition())+1000, 0),mediaPlayer.getDuration()));
        this.mediaPlayer.start();
    }

    public String updateTimeStamp() {
        int currentPositionMilli = mediaPlayer.getCurrentPosition();
        int currentTimeMin = Math.floorDiv(currentPositionMilli, 1000*60);
        int currentTimeSec = Math.floorDiv(currentPositionMilli - currentTimeMin*60*1000, 1000);
        String currentTimeMinString = (currentTimeMin < 10 ? "0" + String.valueOf(currentTimeMin) : String.valueOf(currentTimeMin));
        String currentTimeSecString = (currentTimeSec < 10 ? "0" + String.valueOf(currentTimeSec) : String.valueOf(currentTimeSec));
        return (currentTimeMinString + ":" + currentTimeSecString);
    }
}
