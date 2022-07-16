package com.example.android.miwok;

class word_class {
    private String miwok_words;
    private String english_words;
    private int ImageResID ;
    private int AudioResID;

//    word_class(String miwok,String english) {
//        miwok_words = miwok;
//        english_words = english;
//    }

    word_class(String miwok,String english,int ImageResId,int AudioID) {
        miwok_words = miwok;
        english_words = english;
        ImageResID = ImageResId;
        AudioResID = AudioID;
    }

    public String get_miwok_words() {
        return miwok_words;
    }

    public String get_english_words() {
        return english_words;
    }

    public int get_ImageResID() { return ImageResID; }

    public boolean IsImagePresent() { return ImageResID != -1; }

    public int get_AudioID() { return AudioResID; }

}