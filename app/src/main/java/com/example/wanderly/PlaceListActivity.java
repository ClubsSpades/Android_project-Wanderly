package com.example.wanderly;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlaceListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlaceAdapter adapter;
    private ArrayList<Place> placeList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DatabaseHelper(this);
        placeList = new ArrayList<>();

        loadPlacesFromDatabase();

        adapter = new PlaceAdapter(placeList, new PlaceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Place place) {
                Intent intent = new Intent(PlaceListActivity.this, PlaceDetailActivity.class);
                intent.putExtra("place", place);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadPlacesFromDatabase() {
        Cursor cursor = dbHelper.getAllPlaces();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                int imageResId = cursor.getInt(cursor.getColumnIndex("imageResId"));

                placeList.add(new Place(id, name, location, imageResId, description));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
