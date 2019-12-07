package com.example.hw12_music_player.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private static final String TAG = "repository";
    private static Repository sRepository;
    private List<Music> mMusicList = new ArrayList<>();
    private List<Album> mAlbumList = new ArrayList<>();
    private List<Artist> mArtistList = new ArrayList<>();

    public static Repository getInstance() {
        if (sRepository == null)
            sRepository = new Repository();
        return sRepository;
    }


    public int getUserId(String path) {
        for (Music music : mMusicList) {
            if (music.getPath().equals(path))
                return music.getMusicId();
        }
        return -1;
    }
    public List<Music> getMusicList(Context context) {
        ContentResolver contentResolver = context
                .getApplicationContext().getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query
                (uri, null, selection, null, sortOrder);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    String artistOfAlbum = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String albumKey = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_KEY));
                    int year = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR));
                    //int numberOfSong = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._COUNT));
                    int numberOfSong = 0;
                    Album album = new Album(albumName , artistOfAlbum , albumKey , year , numberOfSong);

                    String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String artistKey = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_KEY));
                   // int numberOfAlbums = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
                    //int numberOfMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
                    int numberOfAlbums = 0;
                    int numberOfMusic = 0;
                    Artist artist = new Artist(artistName , artistKey , numberOfAlbums , numberOfMusic);

                    int musicId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(path);
                    byte[] coverImageInByte = mmr.getEmbeddedPicture();
                    int length = Integer.parseInt
                            (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                    Music music = new Music
                            (musicId, name , path , album , artist , coverImageInByte , length);
                    mMusicList.add(music);
                    Log.d(TAG , "name: " + name + "\npath: " + path + "\nlength: " + length +
                            "\nartistName: " + artistName + "\nartistKey: " + artistKey +
                            "\nnumberOfMusic: " + numberOfMusic +
                            "\nalbum name: " + albumName + "\nartistOfAlbum: " + artistOfAlbum +
                            "\nalbumKey: " + albumKey + "\nyear:" + year + "\nnumberOfSong:" + numberOfSong );

                }
            }
            cursor.close();
        }
        return mMusicList;
    }

    public List<Album> getAlbumList(Context context) {
        mAlbumList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AlbumColumns.ALBUM
                , MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS,
                MediaStore.Audio.AlbumColumns.FIRST_YEAR,
                MediaStore.Audio.AlbumColumns.ALBUM_KEY,
                MediaStore.Audio.AlbumColumns.ARTIST};
        Cursor cursor =context.getContentResolver().query(uri, projection, null, null, null);
        while (cursor.moveToNext()) {
            String albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM));
            String artistOfAlbum = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ARTIST));
            String albumKey = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_KEY));
            int year = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.FIRST_YEAR));
            int numberOfSongs = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS));
            mAlbumList.add(new Album(albumName, artistOfAlbum, albumKey, numberOfSongs, year));
            Log.d("album" , " album name: " + albumName + " artistOfAlbum: " + artistOfAlbum +
                    " albumKey: " + albumKey + "year:" + year + "numberOfSongs: " + numberOfSongs);
        }
        cursor.close();
        return mAlbumList;
    }

    public List<Artist> getArtistList(Context context) {
        mArtistList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.ArtistColumns.ARTIST
                , MediaStore.Audio.ArtistColumns.ARTIST_KEY,
                MediaStore.Audio.ArtistColumns.NUMBER_OF_ALBUMS
                , MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        while (cursor.moveToNext()) {
            String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.ArtistColumns.ARTIST));
            String artistKey = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.ArtistColumns.ARTIST_KEY));
            int numberOfAlbum = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.ArtistColumns.NUMBER_OF_ALBUMS));
            int numberOfTrack = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS));
            mArtistList.add(new Artist(artistName, artistKey, numberOfAlbum, numberOfTrack));
            Log.d("artist" , "artistName: " + artistName + " artistKey: " + artistKey +
                    "numberOfAlbum: " + numberOfAlbum + "numberOfTrack:"+ numberOfTrack);
        }
        cursor.close();
        return mArtistList;
    }
}
