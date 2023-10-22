package mpdam.android.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private ImageView returnIv;

    public String getAnimeID() {
        return animeID;
    }

    public void setAnimeID(String animeID) {
        this.animeID = animeID;
    }

    private String animeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webViewWV);
        returnIv = findViewById(R.id.returnIV);

        String url = getIntent().getStringExtra("VIDEO_URL");
        setAnimeID(getIntent().getStringExtra("ANIME_ID"));


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        returnHome();
    }

    private void returnHome() {
        returnIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WebViewActivity.this, NewsDetailActivity.class);
                intent.putExtra("ANIME_ID", getAnimeID());
                startActivity(intent);
                finish();
            }
        });
    }

}