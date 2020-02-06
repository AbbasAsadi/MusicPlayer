package com.example.music_player.model;

public class Music {
    private int mMusicId;
    private String mName;
    private String mPath;
    private Album mAlbum;
    private Artist mArtist;
    private byte[] mCoverImageInByte;
    private int mLength;

    public Music(int musicId, String name, String path, Album album, Artist artist, byte[] coverImageInByte, int length) {
        mMusicId = musicId;
        mName = name;
        mPath = path;
        mAlbum = album;
        mArtist = artist;
        mCoverImageInByte = coverImageInByte;
        mLength = length;
    }

    public String getLengthInMinute() {
        int minute = mLength / 60000;
        int second = (mLength % 60000) / 1000;
        if (second < 10)
            return minute + ":0" + second;
        else
            return minute + ":" + second;
    }

    public int getMusicId() {
        return mMusicId;
    }

    public String getName() {
        return mName;
    }

    public String getPath() {
        return mPath;
    }

    public Album getAlbum() {
        return mAlbum;
    }

    public Artist getArtist() {
        return mArtist;
    }

    public byte[] getCoverImageInByte() {
        return mCoverImageInByte;
    }

    public int getLength() {
        return mLength;
    }
}
