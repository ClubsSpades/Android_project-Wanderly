package com.example.wanderly;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class DailyQuoteActivity extends AppCompatActivity {

    private ImageView imageQuote;
    private TextView textQuote;
    private Button btnRefresh;

    private static final String QUOTE_API = "https://type.fit/api/quotes";

    private static final String[] IMAGE_URLS = {
            "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80",
            "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80",
            "https://images.unsplash.com/photo-1494526585095-c41746248156?auto=format&fit=crop&w=800&q=80",
            "https://images.unsplash.com/photo-1500534623283-312aade485b7?auto=format&fit=crop&w=800&q=80"
    };

    private JSONArray quotesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_quote);

        imageQuote = findViewById(R.id.imageQuote);
        textQuote = findViewById(R.id.textQuote);
        btnRefresh = findViewById(R.id.btnRefresh);

        btnRefresh.setOnClickListener(v -> loadRandomQuoteAndImage());

        // 先加载名言列表，再显示一条
        new FetchQuotesTask().execute(QUOTE_API);
    }

    private void loadRandomQuoteAndImage() {
        if (quotesArray == null) {
            textQuote.setText("旅行是唯一能让你变得更富有的消费。");
            return;
        }

        Random rand = new Random();

        // 显示随机图片
        String imageUrl = IMAGE_URLS[rand.nextInt(IMAGE_URLS.length)];
        new DownloadImageTask(imageQuote).execute(imageUrl);

        // 显示随机名言
        try {
            JSONObject quoteObj = quotesArray.getJSONObject(rand.nextInt(quotesArray.length()));
            String text = quoteObj.optString("text", "未知名言");
            String author = quoteObj.optString("author", "匿名");
            String display = "\"" + text + "\"\n— " + author;
            textQuote.setText(display);
        } catch (Exception e) {
            e.printStackTrace();
            textQuote.setText("名言解析错误");
        }
    }

    // AsyncTask下载图片
    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public DownloadImageTask(ImageView iv) {
            imageView = iv;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new URL(urlDisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }

    // AsyncTask加载名言列表JSON
    private class FetchQuotesTask extends AsyncTask<String, Void, JSONArray> {
        protected JSONArray doInBackground(String... urls) {
            String urlString = urls[0];
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    int ch;
                    while ((ch = inputStream.read()) != -1) {
                        sb.append((char) ch);
                    }
                    inputStream.close();
                    return new JSONArray(sb.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONArray result) {
            if (result != null) {
                quotesArray = result;
                loadRandomQuoteAndImage();
            } else {
                Toast.makeText(DailyQuoteActivity.this, "名言加载失败", Toast.LENGTH_SHORT).show();
                textQuote.setText("名言加载失败");
            }
        }
    }
}
