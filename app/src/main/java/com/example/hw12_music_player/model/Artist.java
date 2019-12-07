package com.example.hw12_music_player.model;

public class Artist {
    private String mArtistName;
    private String mArtistKey;
    private int mNumberOfAlbums;
    private int mNumberOfMusics;

    public Artist(String artistName, String artistKey, int numberOfAlbums, int numberOfMusics) {
        mArtistName = artistName;
        mArtistKey = artistKey;
        mNumberOfAlbums = numberOfAlbums;
        mNumberOfMusics = numberOfMusics;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public void setArtistName(String artistName) {
        mArtistName = artistName;
    }

    public String getArtistKey() {
        return mArtistKey;
    }

    public void setArtistKey(String artistKey) {
        mArtistKey = artistKey;
    }

    public int getNumberOfAlbums() {
        return mNumberOfAlbums;
    }

    public void setNumberOfAlbums(int numberOfAlbums) {
        mNumberOfAlbums = numberOfAlbums;
    }

    public int getNumberOfMusics() {
        return mNumberOfMusics;
    }

    public void setNumberOfMusics(int numberOfMusics) {
        mNumberOfMusics = numberOfMusics;
    }
}

