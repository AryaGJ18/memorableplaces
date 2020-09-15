 package com.example.aryakrisna.memorableplaces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

 public class MainActivity extends AppCompatActivity {

    ListView listView;
    static List<String> places;
    static List<LatLng> locations;
    static ArrayAdapter<String> arrayAdapter;
     Intent intent;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        intent = new Intent(this, SecondActivity.class);

        locations = new ArrayList<>();
        locations.add(new LatLng(0,0));

        places = new ArrayList<>();
        places.add("Add new place");
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, places);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    intent.putExtra("place", i);
                    startActivity(intent);
            }
        });

    }
}
