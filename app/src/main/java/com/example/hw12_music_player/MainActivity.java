package com.example.hw12_music_player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.hw12_music_player.model.Repository;

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
