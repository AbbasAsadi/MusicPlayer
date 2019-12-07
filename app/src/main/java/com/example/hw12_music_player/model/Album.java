package com.example.hw12_music_player.model;

public class Album {
    private String mAlbumName;
    private String mArtistOfAlbum;
    private String mAlbumKey;
    private int mYear;
    private int mNumberOfSong;

    public Album(String albumName, String artistOfAlbum, String albumKey, int year, int numberOfSong) {
        mAlbumName = albumName;
        mArtistOfAlbum = artistOfAlbum;
        mAlbumKey = albumKey;
        mYear = year;
        mNumberOfSong = numberOfSong;
    }

    public String getAlbumName() {
        return mAlbumName;
    }

    public void setAlbumName(String albumName) {
        mAlbumName = albumName;
    }

    public String getArtistOfAlbum() {
        return mArtistOfAlbum;
    }

    public void setArtistOfAlbum(String artistOfAlbum) {
        mArtistOfAlbum = artistOfAlbum;
    }

    public String getAlbumKey() {
        return mAlbumKey;
    }

    public void setAlbumKey(String albumKey) {
        mAlbumKey = albumKey;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }

    public int getNumberOfSong() {
        return mNumberOfSong;
    }

    public void setNumberOfSong(int numberOfSong) {
        mNumberOfSong = numberOfSong;
    }
}
