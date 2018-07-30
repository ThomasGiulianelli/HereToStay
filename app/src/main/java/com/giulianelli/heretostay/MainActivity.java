package com.giulianelli.heretostay;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.support.design.widget.FloatingActionButton;
import java.util.ArrayList;
import android.content.Context;
import android.widget.MediaController;
import android.widget.VideoView;

import static com.giulianelli.heretostay.ScrollingActivity1.TEXT_KEY;

public class MainActivity extends AppCompatActivity {

    private ListView mainList;
    private VideoView mVideoView;
    private FloatingActionButton fab;
    private Button exitButton;

    static final int REQUEST_VIDEO_CAPTURE = 1;

    // Current playback position (in milliseconds)
    private int mCurrentPosition = 0;

    // Tag for the instance state bundle.
    private static final String PLAYBACK_TIME = "play_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainList = findViewById(R.id.mainList);
        fab = findViewById(R.id.floatingActionButton);
        mVideoView = findViewById(R.id.videoview);
        exitButton = findViewById(R.id.exitButton);

        mVideoView.setVisibility(View.GONE); //hide video player
        exitButton.setVisibility(View.GONE); //hide exit button

        // Check for existence of instance state bundle
        if (savedInstanceState != null) {
            //Get the last known playback time of the video
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
            System.out.println("###################################### current position: " + mCurrentPosition + "##################################");
        }

        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(mVideoView);
        mVideoView.setMediaController(controller);


        // Create Situations
        Situation one = new Situation("Police Stops");
        Situation two = new Situation("Arrests");
        Situation three = new Situation("Protests and Demonstrations");
        Situation four = new Situation("Anti-Muslim Discrimination");
        Situation five = new Situation("Refugee Rights");
        Situation six = new Situation("LGBTQ+ Discrimination");

        // Instantiate array list for different situations
        ArrayList<Situation> situations = new ArrayList<>();
        situations.add(one);
        situations.add(two);
        situations.add(three);
        situations.add(four);
        situations.add(five);
        situations.add(six);

        // Array adapter
        ArrayAdapter<Situation> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                situations);

        mainList.setAdapter(arrayAdapter);

        // Create new activity to display description when list item is clicked
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ScrollingActivity1.class);
                intent.putExtra(TEXT_KEY, mainList.getItemAtPosition(position).toString());
                startActivity(intent);
            }
        });

        // get current context and use it to check if the device has a camera
        Context context = this;
        boolean deviceHasCameraFlag = MyUtilities.checkForCamera(context);

        // Activate camera FAB and exit button functionality if the device has a camera
        if (deviceHasCameraFlag){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dispatchTakeVideoIntent();
                }
            });
            exitButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Exit video playback
                    releasePlayer();
                    mVideoView.setVisibility(View.GONE);
                    exitButton.setVisibility(View.GONE);
                    mainList.setVisibility(View.VISIBLE);
                    fab.show();
                }
            });
        }
        else fab.hide();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Load the media each time onStart() is called if the VideoView is still open
        if (mVideoView.getVisibility() == View.VISIBLE) {
            initializePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        releasePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Pause playback on devices running Android versions older than Android N (7.0, API 24).
        // Allows playback to continue on newer devices when the app is paused but still visible.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current video playback position (in milliseconds) to the
        // instance state bundle.
        outState.putInt(PLAYBACK_TIME, mVideoView.getCurrentPosition());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Setup video playback for the video the user just captured
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            mVideoView.setVideoURI(videoUri);
            mainList.setVisibility(View.GONE); //hide the ListView so it is no longer interactive
            fab.hide(); //hide fab so it is no longer in the way
            mVideoView.setVisibility(View.VISIBLE); //display video player
            exitButton.setVisibility(View.VISIBLE); //display exit button

            initializePlayer();
        }
    }

    // Launches the camera app for video recording
    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    // Begins video playback
    private void initializePlayer() {
        // Restore saved position, if available.
        if (mCurrentPosition > 0) {
            mVideoView.seekTo(mCurrentPosition);
        } else {
            // Shows the first frame of the video instead of blackscreen
            mVideoView.seekTo(1);
        }
        mVideoView.start();
    }

    // Stops video playback
    private void releasePlayer() {
        mVideoView.stopPlayback();
    }
}
