package com.giulianelli.heretostay;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ScrollingActivity1 extends AppCompatActivity {

    public static final String TEXT_KEY = "textKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Toast.makeText(this, (getIntent().getStringExtra(TEXT_KEY)),
                Toast.LENGTH_SHORT).show();

        //display information on the first situation
        if ((getIntent().getStringExtra(TEXT_KEY)).equals("one")){
            TextView textView = (TextView) findViewById(R.id.large_text);
            String description = textView.getText().toString();
            textView.setText(getResources().getString(R.string.large_text1));
        }

        //display information on the second situation
        if ((getIntent().getStringExtra(TEXT_KEY)).equals("two")){
            TextView textView = (TextView) findViewById(R.id.large_text);
            textView.setText(getResources().getString(R.string.large_text2));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
