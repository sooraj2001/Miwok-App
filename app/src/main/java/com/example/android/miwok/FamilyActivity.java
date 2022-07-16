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
import java.util.concurrent.TimeUnit;

public class FamilyActivity extends AppCompatActivity {

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
            Toast.makeText(FamilyActivity.this, "Finished playing", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            ArrayList<word_class> words = new ArrayList<word_class>();
            words.add(new word_class("Father","әpә",R.drawable.family_father,R.raw.family_father));
            words.add(new word_class("Mother","әṭa",R.drawable.family_mother,R.raw.family_mother));
            words.add(new word_class("Son","angsi",R.drawable.family_son,R.raw.family_son));
            words.add(new word_class("Daughter","tune",R.drawable.family_daughter,R.raw.family_daughter));
            words.add(new word_class("Older Brother","taachi",R.drawable.family_older_brother,R.raw.family_older_brother));
            words.add(new word_class("Younger Brother","chalitti",R.drawable.family_younger_brother,R.raw.family_younger_brother));
            words.add(new word_class("Older Sister","teṭe",R.drawable.family_older_sister,R.raw.family_older_sister));
            words.add(new word_class("Younger Sister","kolliti",R.drawable.family_younger_sister,R.raw.family_younger_sister));
            words.add(new word_class("Grandfather","paapa",R.drawable.family_grandfather,R.raw.family_grandfather));
            words.add(new word_class("Grandmother","ama",R.drawable.family_grandmother,R.raw.family_grandmother));
            wordAdapter itemsAdapter = new wordAdapter(this, words, R.color.category_family);

            ListView listView = (ListView) findViewById(R.id.list_family);

            listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer(); // Releases 1st mediaplayer when two list item's are pressed together
                word_class word = words.get(position);

                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT); // Asking Audio Focus for this app

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) { // runs if audio focus gained
                    // Start playback

//                Log.i("Clicked position = ", "" + word.get_english_words());
                    mediaPlayer = MediaPlayer.create(FamilyActivity.this, word.get_AudioID());
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