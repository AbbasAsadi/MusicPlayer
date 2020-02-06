package com.example.music_player;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.music_player.model.MusicPlayer;

import java.io.IOException;

import javax.security.auth.callback.Callback;

public class SoundService extends Service implements Callback {
    public final String TAG = "SoundService";
    private NotificationCompat.Builder mNotification;
    private NotificationManager mNotificationManager;
    private static final int id = 1;
    private MusicPlayer mMusicPlayer;
    //private MediaPlayer mMediaPlayer;
    private boolean isFininsh;
    private int currentPosition;
    private String mCurrentMusicPath;

    public SoundService() {
    }


    @Override
    public void onCreate() {
        mMusicPlayer = MusicPlayer.getInstance(this);
        //mMediaPlayer = mMusicPlayer.getMediaPlayer();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotification = new NotificationCompat.Builder(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle b = intent.getExtras();
        makeNotification(b);
        /*mMediaPlayer = MediaPlayer.create(this, Uri.parse(b.getString("music")));
        mMediaPlayer.start();
        makeNotification(b);*/
        mCurrentMusicPath = intent.getExtras().getString("music");
        Log.e(TAG, "onStartCommand:\n" + mCurrentMusicPath);

        try {
            MusicPlayer.getInstance(this).playMusic(mCurrentMusicPath);
            Log.i(TAG, "onStartCommand:\n" + mCurrentMusicPath);
        } catch (IOException e) {
            Toast.makeText(this, "something wrong!", Toast.LENGTH_SHORT).show();
        }
       //MusicPlayer.getInstance(this).setMediaPlayer(mMediaPlayer);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MusicPlayer.getInstance(this).getMediaPlayer().stop();
        MusicPlayer.getInstance(this).getMediaPlayer().release();
        mNotificationManager.cancel(id);
    }


    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    private void makeNotification(Bundle b){

        mNotification.setSmallIcon(R.mipmap.ic_launcher);
        mNotification.setContentTitle("Play music");
        mNotification.setContentText(b.getString("name"));

        PendingIntent notificationAction = PendingIntent.getActivity
                (this, 0, new Intent
                        (this, MainFragment.class), 0);
        mNotification.setContentIntent(notificationAction);

        mNotificationManager.notify(id, mNotification.build());
    }

}
