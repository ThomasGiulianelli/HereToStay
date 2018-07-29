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
import android.widget.ListView;
import android.support.design.widget.FloatingActionButton;
import java.util.ArrayList;
import android.content.Context;
import android.widget.VideoView;

import static com.giulianelli.heretostay.ScrollingActivity1.TEXT_KEY;

public class MainActivity extends AppCompatActivity {

    private ListView mainList;
    private VideoView mVideoView;
    private FloatingActionButton fab;

    static final int REQUEST_VIDEO_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainList = (ListView) findViewById(R.id.mainList);
        mVideoView = findViewById(R.id.videoview);

        mVideoView.setVisibility(View.GONE); //hide video player

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

        // Display camera FAB if the device has a camera
        if (deviceHasCameraFlag){
            fab = findViewById(R.id.floatingActionButton);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dispatchTakeVideoIntent();
                }
            });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            System.out.println("got video!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            Uri videoUri = intent.getData();
            mVideoView.setVideoURI(videoUri);
            mainList.setVisibility(View.GONE); //hide the ListView so it is no longer interactive
            fab.hide(); //hide fab so it is no longer in the way
            mVideoView.setVisibility(View.VISIBLE); //display video player
            mVideoView.start();
        }
    }

    // Launches the camera app for video recording
    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    // Stops video playback
    private void releasePlayer() {
        mVideoView.stopPlayback();
    }
}
