package com.example.android.miwok;

import android.app.Activity;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class wordAdapter extends ArrayAdapter<word_class> {
    private int colorResourceId;
    public wordAdapter(Activity context, ArrayList<word_class> words, int colorid) {
        super(context,0,words);
        colorResourceId = colorid;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        word_class current_word_class = getItem(position);

        TextView miwok_tv = (TextView) listItemView.findViewById(R.id.miwok_text_view);
        miwok_tv.setText(current_word_class.get_miwok_words());

        TextView english_tv = (TextView) listItemView.findViewById(R.id.english_text_view);
        english_tv.setText(current_word_class.get_english_words());

        ImageView iv = (ImageView) listItemView.findViewById((R.id.Image));
        if(current_word_class.IsImagePresent()) { // checking if image was provided for Phrases activity
            iv.setImageResource(current_word_class.get_ImageResID());
            iv.setVisibility(View.VISIBLE);
        } else {
            iv.setVisibility(View.GONE);
        }

        // Setting background color diffrently for each activity
        View textContainer = listItemView.findViewById(R.id.TextContainer);
        int color = ContextCompat.getColor(getContext(), colorResourceId);
        textContainer.setBackgroundColor(color);

        //Playing Pronounciation feature
//        ImageView playButton = listItemView.findViewById(R.id.playButton);
//        playButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final MediaPlayer[] mediaPlayer = {MediaPlayer.create(getContext(), current_word_class.get_AudioID())};
//                mediaPlayer[0].start();
//
//                mediaPlayer[0].setOnCompletionListener(new MediaPlayer.OnCompletionListener() {  //Async callback after completion of media play comes back here to execute the code
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        Toast.makeText(getContext(), "Finished playing", Toast.LENGTH_SHORT).show();
//                        // If the media player is not null, then it may be currently playing a sound.
//                        if (mediaPlayer[0] != null) {
//                            mediaPlayer[0].release();
//                            mediaPlayer[0] = null;
//                        }
//                    }
//                });
//
//            }
//        });

        return listItemView;
    }
}
