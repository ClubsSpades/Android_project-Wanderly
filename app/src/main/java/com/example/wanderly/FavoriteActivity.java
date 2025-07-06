package com.example.wanderly;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FavoriteActivity extends AppCompatActivity {

    private ListView favoriteListView;
    private DatabaseHelper dbHelper;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        favoriteListView = findViewById(R.id.favoriteListView);
        dbHelper = new DatabaseHelper(this);
        prefs = getSharedPreferences("my_favorites", MODE_PRIVATE);

        loadFavorites();
    }

    private void loadFavorites() {
        Set<String> favoriteIds = prefs.getStringSet("favorites", new HashSet<>());
        if (favoriteIds.isEmpty()) {
            Toast.makeText(this, "暂无收藏", Toast.LENGTH_SHORT).show();
            favoriteListView.setAdapter(null);
            return;
        }

        ArrayList<Place> favoritePlaces = new ArrayList<>();

        for (String idStr : favoriteIds) {
            int id;
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                continue;
            }
            Place place = queryPlaceById(id);
            if (place != null) {
                favoritePlaces.add(place);
            }
        }

        if (favoritePlaces.isEmpty()) {
            Toast.makeText(this, "收藏列表为空或数据错误", Toast.LENGTH_SHORT).show();
            favoriteListView.setAdapter(null);
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                getPlaceNames(favoritePlaces)
        );
        favoriteListView.setAdapter(adapter);
    }

    private Place queryPlaceById(int id) {
        Cursor cursor = dbHelper.getReadableDatabase().query(
                "places",
                null,
                "id = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );
        Place place = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow("imageResId"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                place = new Place(id, name, location, imageResId, description);
            }
            cursor.close();
        }
        return place;
    }

    private ArrayList<String> getPlaceNames(ArrayList<Place> places) {
        ArrayList<String> names = new ArrayList<>();
        for (Place p : places) {
            names.add(p.name);
        }
        return names;
    }
}
