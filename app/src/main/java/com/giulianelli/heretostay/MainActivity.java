package com.giulianelli.heretostay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;

import static com.giulianelli.heretostay.ScrollingActivity1.TEXT_KEY;

public class MainActivity extends AppCompatActivity {

    private ListView mainList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainList = (ListView) findViewById(R.id.mainList);

        //create Situations
        Situation one = new Situation("Police Stops");
        Situation two = new Situation("Arrests");
        Situation three = new Situation("Protests and Demonstrations");
        Situation four = new Situation("Anti-Muslim Discrimination");
        Situation five = new Situation("Refugee Rights");
        Situation six = new Situation("LGBTQ+ Discrimination");

        //instantiate array list for different situations
        ArrayList<Situation> situations = new ArrayList<>();
        situations.add(one);
        situations.add(two);
        situations.add(three);
        situations.add(four);
        situations.add(five);
        situations.add(six);

        //array adapter
        ArrayAdapter<Situation> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                situations);

        mainList.setAdapter(arrayAdapter);

        //create new activity to display description when list item is clicked
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ScrollingActivity1.class);
                intent.putExtra(TEXT_KEY, mainList.getItemAtPosition(position).toString());
                startActivity(intent);
            }
        });

    }
}
