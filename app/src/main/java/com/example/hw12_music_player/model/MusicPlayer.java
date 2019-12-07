package com.example.hw12_music_player.model;

import android.content.ContentUris;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;


public class MusicPlayer extends MediaPlayer {
    public static final String CONTENT_MEDIA_EXTERNAL_AUDIO_ALBUMART = "content://media/external/audio/albumart";
    public static final Uri EXTERNAL_MUSIC_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private static MusicPlayer sMusicPlayer;
    public static MediaPlayer mMediaPlayer;
    private Music mMusic;
    private MusicPlayerCallback mCallback;
    private String mCurrentPath;
    private Context mContext;

    private MusicPlayer(Context context) {
        this.mMediaPlayer = new MediaPlayer();
        this.mContext = context;
    }

    public static MusicPlayer getInstance(Context context) {
        if (sMusicPlayer == null)
            sMusicPlayer = new MusicPlayer(context);
        return sMusicPlayer;
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        mMediaPlayer = mediaPlayer;
    }

    public static String getStringTime(int seconds) {
        int minutes = seconds / 60;
        int secondReminder = seconds % 60;
        return (minutes < 10 ? "0" + minutes : minutes) + ":" + (secondReminder < 10 ? "0" + secondReminder : secondReminder);

        /*Cursor cursor = contentResolver.query(EXTERNAL_MUSIC_URI, null, null, null, null);
        List<Music> tracks = new ArrayList<>();
        List<Album> albums = new ArrayList<>();
        List<Artist> artists = new ArrayList<>();

        while (cursor.moveToNext()) {

            // Music Detail
            String trackId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String trackTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String trackAlbum = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            String trackArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String trackLength = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

            // Music Cover Uri
            Uri uri = ContentUris.withAppendedId(Uri.parse(CONTENT_MEDIA_EXTERNAL_AUDIO_ALBUMART), Long.parseLong(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))));

            // Music
            Music thisLoopTrack = new Music(Long.parseLong(trackId), trackTitle, trackAlbum, trackArtist, uri, Integer.parseInt(trackLength), null);

            // Albums
            Album thisLoopAlbum = null;

            boolean albumExists = false;
            for (Album album : albums) {
                if (album.getAlbumTitle().equals(trackAlbum)) {
                    albumExists = true;
                    thisLoopAlbum = album;
                    album.getTracks().add(Long.parseLong(trackId));
                }
            }
            if (!albumExists) {
                thisLoopAlbum = new Album(trackAlbum, trackArtist, uri);
                albums.add(thisLoopAlbum);
            }

            // Artist
            boolean artistExists = false;
            for (Artist artist : artists) {
                if (artist.getArtistName().equals(trackArtist)) {

                    artistExists = true;
                    artist.getTracks().add(thisLoopTrack.getTrackId());


                    // Album For Each Artist
                    boolean artistAlbumExists = false;
                    for (UUID artistAlbums : artist.getAlbums()) {
                        if (thisLoopAlbum.getAlbumId().equals(artistAlbums)) {
                            artistAlbumExists = true;
                            break;
                        }
                    }
                    if (!artistAlbumExists) {
                        artist.getAlbums().add(thisLoopAlbum.getAlbumId());
                    }

                }
            }
            if (!artistExists) {
                artists.add(new Artist(trackArtist, uri));
            }


            tracks.add(thisLoopTrack);

        }

        cursor.close();

        MusicRepository.getInstance().setTracks(tracks);
        MusicRepository.getInstance().setAlbumList(albums);
        MusicRepository.getInstance().setArtistList(artists);*/
    }

    public void playMusic(String path) throws IOException {
        mMediaPlayer.stop();

        mCurrentPath = path;

        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Repository.getInstance().getUserId(mCurrentPath));

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setDataSource(mContext.getApplicationContext(), contentUri);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                String nextTrackPath = mCallback.getNextTrack();
                try {
                    playMusic(nextTrackPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mCallback.updateUiAutoSkip();
            }
        });
        mMediaPlayer.prepare();
        mMediaPlayer.start();

        /*//getActivity().stopService(new Intent(getActivity(), SoundService.class));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            SoundService soundService = getActivity().getSystemService(SoundService.class);
            mMediaPlayer = soundService.getMusicPlayer();
        }
        if (!mPlayedItem.contains(lastPosition))
            mPlayedItem.add(lastPosition);
        //Music tmpMusic = mMusicList.get(lastPosition);
        byte[] dataCover = tmpMusic.getCoverImageInByte();
        if (dataCover != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(dataCover,
                    0, dataCover.length);
            mImageViewMusicCover.setImageBitmap(bitmap);
        }else {
            mImageViewMusicCover.setImageResource(R.drawable.default_cover);
        }

        mTextViewMusicName.setText(tmpMusic.getName());
        mTextViewLength.setText(tmpMusic.getLengthInMinute());

        path = new Intent(getActivity(), SoundService.class);
        path.putExtra("music", mMusicList.get(lastPosition).getPath());
        getActivity().startService(path);
        isPlaying = true;
        mButtonPlay.setImageResource(R.drawable.ic_pause_black_24dp);*/
    }

    public String getCurrentPath() {
        return mCurrentPath;
    }

    public int pauseMusic(){
        mMediaPlayer.pause();
        return mMediaPlayer.getCurrentPosition();
    }
    public void playAfterPause(int seekTime) {
        mMediaPlayer.seekTo(seekTime);
        mMediaPlayer.start();
    }
    public interface MusicPlayerCallback {
        String getNextTrack();

        void updateUiAutoSkip();
    }

}



