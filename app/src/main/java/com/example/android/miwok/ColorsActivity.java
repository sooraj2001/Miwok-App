package com.example.android.miwok;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    private AudioManager mAudioManager;

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // Permanent loss of audio focus
                        // Pause playback immediately
                        releaseMediaPlayer();
                    }
                    else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(0); // Start audio file frm beginning
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // Your app has been granted audio focus again
                        // Raise volume to normal, restart playback if necessary
                        mediaPlayer.start();
                    }
                }
            };

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
            Toast.makeText(ColorsActivity.this, "Finished playing", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        ArrayList<word_class> words = new ArrayList<word_class>();
        words.add(new word_class("Black","kululli",R.drawable.color_black,R.raw.color_black));
        words.add(new word_class("Brown","ṭakaakki",R.drawable.color_brown,R.raw.color_brown));
        words.add(new word_class("Gray","ṭopoppi",R.drawable.color_gray,R.raw.color_gray));
        words.add(new word_class("Dusty Yellow","ṭopiisә",R.drawable.color_dusty_yellow,R.raw.color_dusty_yellow));
        words.add(new word_class("Green","chokokki",R.drawable.color_green,R.raw.color_green));
        words.add(new word_class("Mustard Yellow","chiwiiṭә",R.drawable.color_mustard_yellow,R.raw.color_mustard_yellow));
        words.add(new word_class("Red","weṭeṭṭi",R.drawable.color_red,R.raw.color_red));
        words.add(new word_class("White","kelelli",R.drawable.color_white,R.raw.color_white));
        wordAdapter itemsAdapter = new wordAdapter(this, words, R.color.category_colors);

        ListView listView = (ListView) findViewById(R.id.list_colors);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                word_class word = words.get(position);
                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT); // Asking Audio Focus for this app

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) { // runs if audio focus gained
                    // Start playback
                    //Log.i("Clicked position = ", "" + word.get_english_words());
                    mediaPlayer = MediaPlayer.create(ColorsActivity.this, word.get_AudioID());
                    mediaPlayer.start();

                    mediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

//        LinearLayout rootView = (LinearLayout) findViewById(R.id.rootView);
//
//        TextView wordView = new TextView(this);
//        for(int i=0;i<words.size();i++) {
//            wordView.append("\n    "+words.get(i));
//        }
//        rootView.addView(wordView);
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener); // both mCompletionListener and onStop have the release method called , so Audio Focus is abandoned in both cases here
        }
    }
}