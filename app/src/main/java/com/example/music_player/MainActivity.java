package com.example.music_player;

import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class MainActivity extends SingleFragmentActivity {

    @Override
    public Fragment getFragment() {
        return MainFragment.newInstance();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public int getResourceId() {
        return R.id.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Repository.getInstance().getMusicList(this);
        //Repository.getInstance().getArtistList(this);
        //Repository.getInstance().getAlbumList(this);
    }
}
