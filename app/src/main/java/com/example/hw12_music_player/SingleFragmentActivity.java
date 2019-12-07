package com.example.hw12_music_player;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    public abstract Fragment getFragment();
    public abstract int getContentView();
    public abstract int getResourceId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        getSupportFragmentManager().beginTransaction()
                .replace(getResourceId() , getFragment())
                .commit();
    }
}
