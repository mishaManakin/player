package com.example.myapplication456;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private final String DATA_STREAM = "http://ep128.hostingradio.ru:8030/ep128";
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private TextView textOut;
    private Switch switchLoop;

    private String nameAudio = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textOut = findViewById(R.id.textOut);
        switchLoop = findViewById(R.id.switchLoop);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        switchLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mediaPlayer != null) {
                    mediaPlayer.setLooping(b);
                }
            }
        });
    }


    public void onClick(View view) {
        if (mediaPlayer == null) return;

        switch (view.getId()) {
            case R.id.btnResume:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start(); // метод возобновления проигрывания
                }
                break;
            case R.id.btnPause:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause(); // метод паузы
                }
                break;
            case R.id.btnStop:
                mediaPlayer.stop(); // метод остановки
                break;
            case R.id.btnForward:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000); // переход к определённой позиции трека
                // mediaPlayer.getCurrentPosition() - метод получения текущей позиции
                break;
            case R.id.btnBack:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000); // переход к определённой позиции трека
                break;
        }
        // информативный вывод информации
        textOut.setText(nameAudio + "\n(проигрывание " + mediaPlayer.isPlaying() + ", время " + mediaPlayer.getCurrentPosition()
                + ",\nповтор " + mediaPlayer.isLooping() + ", громкость " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + ")");
    }


    public void onClickSource(View view) {
        releaseMediaPlayer();
        try {
            switch (view.getId()) {
                case R.id.btnStream:

                    mediaPlayer = new MediaPlayer();

                    mediaPlayer.setDataSource(DATA_STREAM);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.prepareAsync();

                    nameAudio = "Крутая мелодия";




                    break;
                case R.id.btnRAW:
                    mediaPlayer = MediaPlayer.create(this,R.raw.flight_of_the_bumblebee);
                    mediaPlayer.start();
                    nameAudio = "классическая  мелодия";



                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mediaPlayer==null) return;

        mediaPlayer.setLooping(switchLoop.isChecked());
        mediaPlayer.setOnCompletionListener(this);

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
