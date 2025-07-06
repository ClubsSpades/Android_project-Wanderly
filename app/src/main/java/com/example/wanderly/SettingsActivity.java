package com.example.wanderly;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat switchNightMode;
    private Button btnClearCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 先根据保存的设置切换主题，保证Activity启动时主题正确
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean nightMode = prefs.getBoolean("night_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                nightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchNightMode = findViewById(R.id.switch_night_mode);
        btnClearCache = findViewById(R.id.btn_clear_cache);

        switchNightMode.setChecked(nightMode);

        switchNightMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("night_mode", isChecked).apply();

            // 切换主题需要用AppCompatDelegate.setDefaultNightMode，recreate()才有效
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            recreate(); // 重新创建Activity应用新主题
        });

        btnClearCache.setOnClickListener(v -> {
            clearAppCache();
            Toast.makeText(this, "缓存已清除", Toast.LENGTH_SHORT).show();
        });
    }

    private void clearAppCache() {
        // 示例：删除应用缓存目录的文件（根据你项目实际需求调整）
        try {
            java.io.File cacheDir = getCacheDir();
            if (cacheDir != null && cacheDir.isDirectory()) {
                for (java.io.File child : cacheDir.listFiles()) {
                    child.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
