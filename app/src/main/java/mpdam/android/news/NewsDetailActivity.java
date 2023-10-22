package mpdam.android.news;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsDetailActivity extends AppCompatActivity {
    private ImageView articleImage;
    private TextView animeName;
    private Button score;
    private TextView scoredBy;
    private TextView synopsis;

    private ImageView trailerIv;

    private Button playVideoBtn;

    private ImageView returnIv;

    private TextView statusAnime, episodesAnime, durationAnime;

    private String animeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        articleImage = findViewById(R.id.articleImageIV);
        animeName = findViewById(R.id.animeNameTV);
        score = findViewById(R.id.scoreBtn);
        scoredBy = findViewById(R.id.scoredByTV);
        synopsis = findViewById(R.id.synopsisTV);
        trailerIv = findViewById(R.id.trailerIV);
        playVideoBtn = findViewById(R.id.playVideoBtn);
        returnIv = findViewById(R.id.returnIV);
        statusAnime = findViewById(R.id.statusAnimeTV);
        episodesAnime = findViewById(R.id.episodesAnimeTV);
        durationAnime = findViewById(R.id.durationAnimeTV);

        this.animeID = getIntent().getStringExtra("ANIME_ID");
        getAnime(animeID);
        returnHome();
    }

    private void returnHome() {
        returnIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsDetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getAnime(String id) {
        String url = "https://api.jikan.moe/v4/anime/" + id + "/full";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject animeObject = response.getJSONObject("data");
                    long idObj = animeObject.getLong("mal_id");
                    String titleObj = animeObject.getString("title");
                    String imageUrlObj = animeObject.getJSONObject("images").getJSONObject("jpg").getString("image_url");
                    String descriptionObj = animeObject.getString("synopsis");
                    Double scoreObj = animeObject.getDouble("score");
                    long scored_byObj = animeObject.getInt("scored_by");
                    String statusObj = animeObject.getString("status");
                    String episodesObj = animeObject.getString("episodes");
                    String durationObj = animeObject.getString("duration");

                    String placerHolderTrailer = animeObject.getJSONObject("trailer").getJSONObject("images").getString("maximum_image_url");
                    String trailerUrl = animeObject.getJSONObject("trailer").getString("url");
                    String trailerID = animeObject.getJSONObject("trailer").getString("youtube_id");
                    System.out.println("placeholder");
                    System.out.println(placerHolderTrailer);
                    System.out.println("trailer");
                    System.out.println(trailerUrl);

                    Picasso.get().load(imageUrlObj)
                            .error(R.drawable.baseline_image_not_supported_24)
                            .placeholder(R.drawable.baseline_image_not_supported_24)
                            .into(articleImage);

                    animeName.setText(titleObj);
                    score.setText(String.valueOf(scoreObj));
                    scoredBy.setText(String.valueOf(scored_byObj));
                    statusAnime.setText(statusObj);
                    episodesAnime.setText(episodesObj);
                    durationAnime.setText(durationObj);

                    JSONArray streamingArray = animeObject.getJSONArray("streaming");
                    List<Anime> streamingPlatforms = new ArrayList<>();

                    for (int i = 0; i < streamingArray.length(); i++) {
                        JSONObject platformObject = streamingArray.getJSONObject(i);
                        String platformName = platformObject.getString("name");
                        Anime streamingPlatform = new Anime(platformName);
                        streamingPlatforms.add(streamingPlatform);
                    }

                    LinearLayout streamingIconsLayout = findViewById(R.id.streamingIconsLayout);

                    for (Anime platform : streamingPlatforms) {
                        ImageView platformIcon = new ImageView(NewsDetailActivity.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);
                        platformIcon.setLayoutParams(params);

                        platformIcon.setPadding(15, 0, 15, 0);

                        streamingIconsLayout.setBackgroundColor(ContextCompat.getColor(NewsDetailActivity.this, R.color.gray));

                        if ("Netflix".equals(platform.getStreaming())) {
                            platformIcon.setImageResource(R.drawable.icons8_netflix_48);
                        } else if ("Crunchyroll".equals(platform.getStreaming())) {
                            platformIcon.setImageResource(R.drawable.icons8_crunchyroll_48);
                        } else if ("Hulu".equals(platform.getStreaming())) {
                            platformIcon.setImageResource(R.drawable.icons8_hulu_48);
                        } else if ("Funimation".equals(platform.getStreaming())) {
                            platformIcon.setImageResource(R.drawable.icons8_funimation_48);
                        }

                        streamingIconsLayout.addView(platformIcon);
                    }

                    synopsis.setText(descriptionObj);

                    Picasso.get().load(placerHolderTrailer)
                            .error(R.drawable.baseline_image_not_supported_24)
                            .into(trailerIv);
                    playVideoBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showVideoPlaybackOptions(trailerUrl, trailerID);
                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        Volley.newRequestQueue(this).add(request);
    }

    private void showVideoPlaybackOptions(String trailerUrl, String trailerID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Playback Option");

        builder.setPositiveButton("Play in App", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                launchVideoInApp(trailerUrl);
            }
        });

        builder.setNegativeButton("Open in YouTube", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openVideoInYouTube(trailerID);
            }
        });

        builder.create().show();
    }

    private void launchVideoInApp(String videoUrl) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("VIDEO_URL", videoUrl);
        intent.putExtra("ANIME_ID", this.animeID);
        startActivity(intent);
    }

    private void openVideoInYouTube(String youtubeVideoId) {
        try {
            Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubeVideoId));
            startActivity(youtubeIntent);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + youtubeVideoId));
            startActivity(intent);
        }
    }
}