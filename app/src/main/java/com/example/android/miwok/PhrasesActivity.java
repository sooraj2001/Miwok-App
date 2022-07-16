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

public class PhrasesActivity extends AppCompatActivity {

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
            Toast.makeText(PhrasesActivity.this, "Finished playing", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrases);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        ArrayList<word_class> words = new ArrayList<word_class>();
        words.add(new word_class("Where are you going?","minto wuksus",-1,R.raw.phrase_where_are_you_going));
        words.add(new word_class("What is your name?","tinnә oyaase'nә",-1,R.raw.phrase_what_is_your_name));
        words.add(new word_class("My name is...","oyaaset...",-1,R.raw.phrase_my_name_is));
        words.add(new word_class("How are you feeling?","michәksәs?",-1,R.raw.phrase_how_are_you_feeling));
        words.add(new word_class("I’m feeling good.","kuchi achit",-1,R.raw.phrase_im_feeling_good));
        words.add(new word_class("Are you coming?","әәnәs'aa?",-1,R.raw.phrase_are_you_coming));
        words.add(new word_class("Yes, I’m coming.","hәә’ әәnәm",-1,R.raw.phrase_yes_im_coming));
        words.add(new word_class("I’m coming.","әәnәm",-1,R.raw.phrase_im_coming));
        words.add(new word_class("Let’s go.","yoowutis",-1,R.raw.phrase_lets_go));
        words.add(new word_class("Come here.","әnni'nem",-1,R.raw.phrase_come_here));
        wordAdapter itemsAdapter = new wordAdapter(this, words, R.color.category_phrases);

        ListView listView = (ListView) findViewById(R.id.list_phrases);

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
                    mediaPlayer = MediaPlayer.create(PhrasesActivity.this, word.get_AudioID());
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