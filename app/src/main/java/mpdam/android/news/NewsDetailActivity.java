package mpdam.android.news;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class NewsDetailActivity extends AppCompatActivity {
    TextView testAnimeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        String animeID = getIntent().getStringExtra("ANIME_ID");
         testAnimeId = findViewById(R.id.testAnimeId);
        testAnimeId.setText(animeID);

    }
}