package com.example.noxmusicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class NoxPlayer extends AppCompatActivity {

    ImageButton play, prev, next, loop, shuffle;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private ViewFlipper viewFlipper;
    ArrayList<File> songs;
    String textContent;
    int position;
    int flag = 0;
    Thread updateSeek;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nox_player);


        play = findViewById(R.id.button);
        loop = findViewById(R.id.button2);
        shuffle = findViewById(R.id.button3);
        next = findViewById(R.id.button4);
        prev = findViewById(R.id.button5);
        seekBar = findViewById(R.id.seekBar);
        viewFlipper = findViewById(R.id.viewFlipper);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songList");
        textContent = intent.getStringExtra("currentSong");
        textView4.setText(textContent);

        position = intent.getIntExtra("position", 0);
        Uri uri = Uri.parse(songs.get(position).toString());

        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(mediaPlayer.getDuration());
                int Durancion = mediaPlayer.getDuration();
                textView2.setText(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(Durancion), TimeUnit.MILLISECONDS.toSeconds(Durancion) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Durancion))));
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mediaPlayer.seekTo(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                });
            }
        });

        // Play Button Code here :)

        viewFlipper.startFlipping();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.setFlipInterval(2000);
                if (mediaPlayer.isPlaying()) {
                    play.setImageDrawable(getResources().getDrawable(R.drawable.play));
                    mediaPlayer.pause();
                    viewFlipper.stopFlipping();
                    flag =1;
                } else {
                    play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                    mediaPlayer.start();
                    viewFlipper.startFlipping();
                    flag=0;
                }
            }
        });

        // Loop Button Code here :)

        loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mediaPlayer.isLooping()) {
                    mediaPlayer.setLooping(true);
                    Toast.makeText(NoxPlayer.this, "Music is in Loop", Toast.LENGTH_SHORT).show();
                    loop.setImageDrawable(getResources().getDrawable(R.drawable.loop_repete));
                } else {
                    mediaPlayer.setLooping(false);
                    Toast.makeText(NoxPlayer.this, "Music is not in Loop", Toast.LENGTH_SHORT).show();
                    loop.setImageDrawable(getResources().getDrawable(R.drawable.loop));
                }
            }
        });

        // Previous Button Code here :)

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                if (position != 0) {
                    position = position - 1;
                } else {
                    position = songs.size() - 1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                textContent = songs.get(position).getName().toString();
                textView4.setText(textContent);
                int Durancion = mediaPlayer.getDuration();
                textView2.setText(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(Durancion), TimeUnit.MILLISECONDS.toSeconds(Durancion) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Durancion))));
            }
        });

        // Next Button Code here :)

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                if (position != songs.size() - 1) {
                    position = position + 1;
                } else {
                    position = 0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                textContent = songs.get(position).getName().toString();
                textView4.setText(textContent);
                int Durancion = mediaPlayer.getDuration();
                textView2.setText(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(Durancion), TimeUnit.MILLISECONDS.toSeconds(Durancion) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Durancion))));
                play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
            }
        });

        // Shuffle Code here :)

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                Random rand = new Random();
                for (int i = songs.size() - 1; i > 0; i--) {
                    int index = rand.nextInt(i + 1);
                    String temp = String.valueOf(songs.get(index));
                    songs.set(index, songs.get(i));
                    songs.set(i, new File(temp));
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                textContent = songs.get(position).getName().toString();
                textView4.setText(textContent);
                int Durancion = mediaPlayer.getDuration();
                textView2.setText(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(Durancion), TimeUnit.MILLISECONDS.toSeconds(Durancion) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Durancion))));
                play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
            }
        });

        // Seekbar Code here :)

        updateSeek = new Thread() {
            @Override
            public void run() {
                int currentPosition = 0;
                try {
                    int Durancion = mediaPlayer.getDuration();
                    while (currentPosition < mediaPlayer.getDuration()) {
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(500);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();

        seekBar.setMax(mediaPlayer.getDuration());

        // Create a new handler
        Handler handler = new Handler();

        // Create a new runnable
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    textView3.setText(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(currentPosition), TimeUnit.MILLISECONDS.toSeconds(currentPosition) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentPosition))));

                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                } else {
                    if (flag != 1) {
                        seekBar.post(new Runnable() {
                            public void run() {
                                next.performClick();
                            }
                        });
                        flag = 0;
                    }
                }
                // Post the runnable again after 1 second
                handler.postDelayed(this, 1000);
            }
        };
        // Start the runnable
        handler.post(runnable);

    }
}