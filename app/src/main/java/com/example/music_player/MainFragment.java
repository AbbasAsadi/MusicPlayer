package com.example.music_player;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_player.model.Music;
import com.example.music_player.model.MusicPlayer;
import com.example.music_player.model.Repository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private static final String IS_PLAYING = "isPlaying";

    //private MusicPlayer mMusicPlayer;
    private FloatingActionButton mButtonPlay;
    private SeekBar mSeekBar;
    private ImageView mImageViewMusicCover;
    private ImageView mImageViewPrevious;
    private ImageView mImageViewNext;
    private ImageView mImageViewShuffle;
    private ImageView mImageViewRepeat;
    private TextView mTextViewLength;
    private TextView mTextViewTime;
    private TextView mTextViewMusicName;
    private TextView mTextViewArtistName;
    private Intent path;
    private Context mContext;
    private Handler myHandler = new Handler();

    private List<Music> mMusicList;
    private List<Integer> mPlayedItem;

    private int lastPosition = 1;
    private int lastPlayedMusicIndex = -1;
    private int mElapsedTime;
    private int mProgress = 0;
    private boolean isPlaying;
    private boolean isRepeatOne;
    private boolean isRepeatAll;
    private boolean isShuffle;
    private boolean isPaused;


    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {

        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_PLAYING, isPlaying);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mMusicList = new ArrayList<>();
        mPlayedItem = new ArrayList<>();
        mMusicList = Repository.getInstance().getMusicList(mContext);
        isPlaying = false;
        if (savedInstanceState != null)
            isPlaying = savedInstanceState.getBoolean(IS_PLAYING);
        Log.e("TAG", isPlaying + " in onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initUi(view);
        if (isPlaying) {
            mButtonPlay.setImageResource(R.drawable.ic_pause_black_24dp);
        } else {
            mButtonPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
        Log.e("TAG", isPlaying + " in onCreateView");

        mButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    isPlaying = false;
                    isPaused = true;
                    mButtonPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    mElapsedTime = MusicPlayer.getInstance(mContext).pauseMusic();

                } else if (isPaused) {
                    isPlaying = true;
                    isPaused = false;
                    mButtonPlay.setImageResource(R.drawable.ic_pause_black_24dp);
                    MusicPlayer.getInstance(mContext).playAfterPause(mElapsedTime);

                } else {
                    playMusic();
                }
                Log.e("TAG", isPlaying + " in onClick");

            }
        });
        mImageViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNext();
            }
        });

        mImageViewPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPreviousMusic();
                Log.e("TAG2", mPlayedItem.toString());
                playMusic();
            }
        });

        mImageViewShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShuffle) {
                    isShuffle = false;
                    mImageViewShuffle.setImageResource(R.drawable.ic_shuffle_off_24dp);
                } else {
                    isShuffle = true;
                    mImageViewShuffle.setImageResource(R.drawable.ic_shuffle_on_24dp);
                }

            }
        });

        mImageViewRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRepeatOne && !isRepeatAll) {
                    isRepeatOne = true;
                    mImageViewRepeat.setImageResource(R.drawable.ic_repeat_one_black_24dp);
                } else if (isRepeatOne) {
                    isRepeatOne = false;
                    isRepeatAll = true;
                    mImageViewRepeat.setImageResource(R.drawable.ic_repeat_on_24dp);
                } else if (isRepeatAll) {
                    isRepeatAll = false;
                    mImageViewRepeat.setImageResource(R.drawable.ic_repeat_off_24dp);
                }
            }
        });


        return view;
    }
    public void playNext() {
        if (isShuffle) {
            lastPosition = generateRandomNumber();
            Toast.makeText(getActivity(), lastPosition + "", Toast.LENGTH_SHORT).show();
        } else {
            lastPosition++;
            convertLastPositionValue();
        }
        lastPlayedMusicIndex++;
        playMusic();
    }

    private void findPreviousMusic() {
        if (lastPlayedMusicIndex == -1 || mPlayedItem.size() < 2) {
            if (isShuffle)
                lastPosition = generateRandomNumber();
            else {
                lastPosition--;
                convertLastPositionValue();
            }
        } else {
            lastPosition = mPlayedItem.get(lastPlayedMusicIndex);
            lastPlayedMusicIndex--;
        }

    }

    private void convertLastPositionValue() {
        if (lastPosition >= mMusicList.size()) {
            lastPosition = 0;
        } else if (lastPosition < 0) {
            lastPosition = mMusicList.size() - 1;
        }
    }

    private int generateRandomNumber() {
        while (true) {
            int randomNumber = new Random().nextInt(mMusicList.size());
            if (mPlayedItem.contains(randomNumber) && mPlayedItem.size() == mMusicList.size()) {
                mPlayedItem.clear();
                return randomNumber;
            } else if (mPlayedItem.contains(randomNumber))
                continue;
            return randomNumber;
        }
    }

    private void playMusic() {
        getActivity().stopService(new Intent(getActivity(), SoundService.class));
        if (!mPlayedItem.contains(lastPosition))
            mPlayedItem.add(lastPosition);
        Music tmpMusic = mMusicList.get(lastPosition);
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
        mTextViewArtistName.setText(tmpMusic.getArtist().getArtistName());

        path = new Intent(getActivity(), SoundService.class);
        path.putExtra("music",  tmpMusic.getPath());
        getActivity().startService(path);
        isPlaying = true;
        mButtonPlay.setImageResource(R.drawable.ic_pause_black_24dp);
    }



    private void initUi(View view) {
        mButtonPlay = view.findViewById(R.id.play_button);
        mImageViewMusicCover = view.findViewById(R.id.image_cover);
        mImageViewPrevious = view.findViewById(R.id.previous_button);
        mImageViewNext = view.findViewById(R.id.next_button);
        mImageViewShuffle = view.findViewById(R.id.shuffle_button);
        mImageViewRepeat = view.findViewById(R.id.repeat_button);
        mTextViewTime = view.findViewById(R.id.time_tv);
        mTextViewLength = view.findViewById(R.id.length_tv);
        mTextViewMusicName = view.findViewById(R.id.music_name_tv);
        mTextViewArtistName = view.findViewById(R.id.artist_name_tv);
        mSeekBar = view.findViewById(R.id.seek_bar);
    }
}
