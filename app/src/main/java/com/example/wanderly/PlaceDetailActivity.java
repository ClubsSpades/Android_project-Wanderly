package com.example.wanderly;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class PlaceDetailActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        Place place = (Place) getIntent().getSerializableExtra("place");

        ImageView imageView = findViewById(R.id.detailImage);
        TextView nameView = findViewById(R.id.detailName);
        TextView descView = findViewById(R.id.detailDescription);
        Button playPause = findViewById(R.id.btnPlayPause);
        Button btnFavorite = findViewById(R.id.btnFavorite);
        seekBar = findViewById(R.id.musicSeekBar);

        imageView.setImageResource(place.imageResId);
        nameView.setText(place.name);
        descView.setText(place.description);

        mediaPlayer = MediaPlayer.create(this, R.raw.place_music); // 替换为具体音频
        if (mediaPlayer != null) {
            seekBar.setMax(mediaPlayer.getDuration());
        }

        playPause.setOnClickListener(v -> {
            if (isPlaying) {
                mediaPlayer.pause();
                playPause.setText("播放");
            } else {
                mediaPlayer.start();
                playPause.setText("暂停");
                updateSeekBar();
            }
            isPlaying = !isPlaying;
        });

        btnFavorite.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("my_favorites", MODE_PRIVATE);
            Set<String> favorites = prefs.getStringSet("favorites", new HashSet<>());
            Set<String> newFavorites = new HashSet<>(favorites);

            String idStr = String.valueOf(place.id); // 一定是id，不是name

            if (newFavorites.contains(idStr)) {
                Toast.makeText(this, "已在收藏列表中", Toast.LENGTH_SHORT).show();
            } else {
                newFavorites.add(idStr);
                prefs.edit().putStringSet("favorites", newFavorites).apply();
                Toast.makeText(this, "已添加到收藏", Toast.LENGTH_SHORT).show();
            }
        });



        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(mp -> {
                playPause.setText("播放");
                isPlaying = false;
            });
        }
    }

    private void updateSeekBar() {
        seekBar.postDelayed(() -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                updateSeekBar();
            }
        }, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
