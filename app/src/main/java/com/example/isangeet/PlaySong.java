package com.example.isangeet;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseek.interrupt();
    }

    TextView textView;
//    ImageView play,previous,next;
    Button button,button2,button3;
    ArrayList songs;
    SeekBar seekBar;
    String textcontent;
    int position;
    Thread updateseek;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textView= findViewById(R.id.textView);
//        play= findViewById(R.id.play);
//        previous= findViewById(R.id.previous);
//        next= findViewById(R.id.next);
        button= findViewById(R.id.button);
        button2= findViewById(R.id.button2);
        button3= findViewById(R.id.button3);
        seekBar= findViewById(R.id.seekBar);
        Intent intent = getIntent();
        Bundle bundel = intent.getExtras();
        songs = (ArrayList)bundel.getParcelableArrayList("songList");
        textcontent = intent.getStringExtra("currentSong");
        textView.setText(textcontent);
        textView.setSelected(true);
        position = intent.getIntExtra("position",0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        updateseek = new Thread(){
            @Override
            public void run() {
                int currentprogress= 0;
                try{
                while(currentprogress<mediaPlayer.getDuration()){
                    currentprogress= mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentprogress);
                    sleep(800);
                }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateseek.start();
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    button2.setText("▶️");
                    mediaPlayer.pause();
                }
                else{
                    button2.setText("⏸️");
                    mediaPlayer.start();
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0){
                    position=position-1;
                }
                else{
                    position=songs.size()-1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                button2.setText("⏸️");
                seekBar.setMax(mediaPlayer.getDuration());
                textcontent = songs.get(position).toString();
                textView.setText(textcontent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=songs.size()-1){
                    position=position+1;
                }
                else{
                    position=0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                button2.setText("⏸️");
                seekBar.setMax(mediaPlayer.getDuration());
                textcontent = songs.get(position).toString();
                textView.setText(textcontent);
            }
        });

    }
}