package com.example.wanderly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.media.MediaPlayer;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer bgMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 背景音乐初始化
        bgMusic = MediaPlayer.create(this, R.raw.bg_music); // 确保 res/raw/bg_music.mp3 存在
        if (bgMusic != null) {
            bgMusic.setLooping(true);
            bgMusic.start();
        } else {
            Toast.makeText(this, "背景音乐加载失败", Toast.LENGTH_SHORT).show();
        }

        Button btnPlaces = findViewById(R.id.btnPlaces);
        btnPlaces.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PlaceListActivity.class);
            startActivity(intent);
        });

        Button btnFavorites = findViewById(R.id.btnFavorites);
        btnFavorites.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
            startActivity(intent);
        });

        Button btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        Button btnDailyQuote = findViewById(R.id.btn_daily_quote);
        btnDailyQuote.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DailyQuoteActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bgMusic != null) {
            bgMusic.stop();
            bgMusic.release();
            bgMusic = null;
        }
    }
}
