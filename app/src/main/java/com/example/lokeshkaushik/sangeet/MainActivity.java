package com.example.lokeshkaushik.sangeet;


import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity implements ActionBar.TabListener, SeekBar.OnSeekBarChangeListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    public static Context contextOfApplication;
    private MusicService musicSrv;
    private SetTimer setTimer;
    private Intent playIntent;
    private boolean musicBound = false;
    private SlidingUpPanelLayout mLayout;
    private View songPlayerView;
    private TextView currentSongTitle;
    private TextView currentSongArtistName;
    private ImageView currentSongImage;
    private ImageView songImage;
    ImageButton btn_play;
    ImageButton btn_previous;
    ImageButton btn_next;
    ImageButton btn_play2;
    private SeekBar seekBar;
    ColorDrawable colorDrawable;
    private Handler mHandler = new Handler();
    private ImageButton btnRandom;
    private ImageButton btnRepeat;
    private boolean isRepeat = false;
    private boolean isRandom = false;
    private TextView currentDuration;
    private TextView totalDuration;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ActionBar actionBar = getSupportActionBar();
        colorDrawable = new ColorDrawable(Color.parseColor("#21232F"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#21232F")));
        contextOfApplication = getApplicationContext();
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        songPlayerView = findViewById(R.id.player_footer);
        mLayout.setDragView(songPlayerView.findViewById(R.id.relativeLayout));
        setTimer = new SetTimer();
        currentSongImage = (ImageView) songPlayerView.findViewById(R.id.current_song_image);
        currentSongTitle = (TextView) songPlayerView.findViewById(R.id.current_song_name);
        currentSongArtistName = (TextView) songPlayerView.findViewById(R.id.current_song_artist_name);
        btn_play = (ImageButton) songPlayerView.findViewById(R.id.btn_play);
        btn_next = (ImageButton) songPlayerView.findViewById(R.id.btn_next);
        btn_play2 = (ImageButton) songPlayerView.findViewById(R.id.btn_play2);
        btn_previous = (ImageButton) songPlayerView.findViewById(R.id.btn_previous);
        btnRandom = (ImageButton) songPlayerView.findViewById(R.id.btn_random);
        btnRepeat = (ImageButton) songPlayerView.findViewById(R.id.btn_repeat);
        songImage = (ImageView) songPlayerView.findViewById(R.id.imageView);
        seekBar = (SeekBar) songPlayerView.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        currentDuration = (TextView) songPlayerView.findViewById(R.id.current_duration);
        totalDuration = (TextView) songPlayerView.findViewById(R.id.total_duration);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("SongPlayer"));

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mReceiver, new IntentFilter("SongDetails"));

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (musicSrv.isPlaying()) {
                    btn_play2.setImageResource(R.mipmap.ic_pause);
                } else {
                    btn_play2.setImageResource(R.mipmap.ic_play);
                }
                actionBar.hide();
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {

                    if (musicSrv.isPlaying()) {
                        btn_play.setImageResource(R.mipmap.ic_pause);
                    } else {
                        btn_play.setImageResource(R.mipmap.ic_play);
                    }
                    btn_play.setEnabled(true);
                    btn_play.setClickable(true);
                    actionBar.show();

                } else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    actionBar.hide();
                    btn_play.setImageResource(R.mipmap.ic_play_btn_background);
                    btn_play.setEnabled(false);
                    btn_play.setClickable(false);


                }
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (musicSrv.isPlaying()) {
                    musicSrv.pauseSong();
                    btn_play.setImageResource(R.mipmap.ic_play);
                } else {
                    musicSrv.resumeSong();
                    btn_play.setImageResource(R.mipmap.ic_pause);
                }
            }
        });
        btn_play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicSrv.isPlaying()) {
                    musicSrv.pauseSong();
                    btn_play2.setImageResource(R.mipmap.ic_play);
                } else {
                    musicSrv.resumeSong();
                    btn_play2.setImageResource(R.mipmap.ic_pause);
                }

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playNext();
            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playPrevious();
            }
        });
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isRepeat){
                    btnRepeat.setImageResource(R.mipmap.ic_not_repeat);
                    musicSrv.setRepeat();
                    isRepeat = false;

                }
                else{
                    btnRepeat.setImageResource(R.mipmap.ic_repeat);
                    musicSrv.setRepeat();
                    isRepeat = true;
                    isRandom = false;
                    btnRandom.setImageResource(R.mipmap.ic_not_random);
                }
            }
        });

        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isRandom){
                    btnRandom.setImageResource(R.mipmap.ic_not_random);
                    musicSrv.setRandom();
                    isRandom = false;
                }
                else{
                    btnRandom.setImageResource(R.mipmap.ic_random);
                    musicSrv.setRandom();
                    isRandom = true;
                    isRepeat =false;
                    btnRepeat.setImageResource(R.mipmap.ic_not_repeat);
                }
            }
        });

        // Set up the action bar.

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);


            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }


    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long currTime = intent.getLongExtra("currentTime", 1);
            long totTime = intent.getLongExtra("totalTime", 1);
            currentDuration.setText(setTimer.milliSecondsToTimer(currTime));
            totalDuration.setText(setTimer.milliSecondsToTimer(totTime));
            int progress = (int) (setTimer.getProgressPercentage(currTime, totTime));
            seekBar.setProgress(progress);
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String albumId = intent.getStringExtra("songImage");
            String songTitle = intent.getStringExtra("songName");
            String artistName = intent.getStringExtra("artistName");
            Picasso.with(context).load(albumId).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(currentSongImage);
            Picasso.with(context).load(albumId).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(songImage);
            currentSongTitle.setText(songTitle);
            currentSongArtistName.setText(artistName);
            btn_play.setImageResource(R.mipmap.ic_pause);
            if (mLayout.getPanelState()==SlidingUpPanelLayout.PanelState.EXPANDED){
                btn_play.setImageResource(R.mipmap.ic_play_btn_background);
            }
            btn_play2.setImageResource(R.mipmap.ic_pause);
        }
    };

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass list

            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(musicSrv.serviceRunnable);

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(musicSrv.serviceRunnable);
        int totalDuration = musicSrv.getPlayer().getDuration();
        int currentPosition = setTimer.progressToTimer(seekBar.getProgress(), totalDuration);
        musicSrv.getPlayer().seekTo(currentPosition);
        musicSrv.updateProgress();

    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());


    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new FragmentSongs();
            if (position == 1)
                return new FragmentArtists();
            if (position == 2)
                return new FragmentAlbum();
            else
                return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Songs";
                case 1:
                    return "Artists";
                case 2:
                    return "Albums";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                View rootView = inflater.inflate(R.layout.fragment_songs, container, false);
                return rootView;
            }
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                View rootView = inflater.inflate(R.layout.fragment_artists, container, false);
                return rootView;
            } else {
                View rootView = inflater.inflate(R.layout.fragment_album, container, false);
                return rootView;
            }
        }
    }


    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Back
            moveTaskToBack(true);
            return true;
        } else {
            // Return
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }


}
