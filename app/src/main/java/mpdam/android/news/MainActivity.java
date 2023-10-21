package mpdam.android.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView newsAnimeRv;
    private List<Anime> animeList = new ArrayList<>();
    private NewsRecyclerAdapter adapter;
    private LinearProgressIndicator progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressIndicator= findViewById(R.id.progress_bar);
        newsAnimeRv = findViewById(R.id.newsAnimeRV);

        setupRecyclerView();
        getLatestAnimes();
    }

    public void changeStateProgressBar(boolean v){
        if(v){
            progressIndicator.setVisibility(View.VISIBLE);
        }else{
            progressIndicator.setVisibility(View.INVISIBLE);
        }
    }
    public void getLatestAnimes(){
        changeStateProgressBar(true);
        String latestAnimes = "https://api.jikan.moe/v4/seasons/now";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, latestAnimes, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    List<Anime> animeListJson = new ArrayList<>();
                    for (int i = 0; i < dataArray.length(); i++) {

                        JSONObject animeObject = dataArray.getJSONObject(i);
                        String title = animeObject.getString("title");
                        String imageUrl = animeObject.getJSONObject("images").getJSONObject("jpg").getString("image_url");
                        String description = animeObject.getString("synopsis");

                        Anime anime = new Anime();
                        anime.setTitle(title);
                        anime.setImageUrl(imageUrl);
                        anime.setDescription(description);

                        animeListJson.add(anime);
                        //System.out.println(anime);
                    }
                    updateRecyclerView(animeListJson);
                    adapter.notifyDataSetChanged();
                    changeStateProgressBar(false);
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
    public void setupRecyclerView(){
        newsAnimeRv.setLayoutManager( new LinearLayoutManager(this));
        adapter = new NewsRecyclerAdapter(animeList);
        newsAnimeRv.setAdapter(adapter);
    }

    public void updateRecyclerView(List<Anime> animeL){
        this.animeList.clear();
        this.animeList.addAll(animeL);
    }
}