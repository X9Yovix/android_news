package mpdam.android.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView titleRelatedToNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressIndicator = findViewById(R.id.progress_bar);
        newsAnimeRv = findViewById(R.id.newsAnimeRV);
        searchView = findViewById(R.id.search_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        titleRelatedToNews = findViewById(R.id.titleRelatedToNewsTV);

        EditText theTextArea = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        theTextArea.setTextColor(Color.WHITE);

        setupRecyclerView();

        String latestAnimes = "/seasons/now";
        getAnimes(latestAnimes);
        setTitleRelatedToNews("Latest anime");
        userSearching();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                String latestAnimes = "/seasons/now";
                getAnimes(latestAnimes);
                setTitleRelatedToNews("Latest anime");
            }
        });
    }

    public void changeStateProgressBar(boolean v) {
        if (v) {
            progressIndicator.setVisibility(View.VISIBLE);
        } else {
            progressIndicator.setVisibility(View.INVISIBLE);
        }
    }

    public void getAnimes(String query) {
        changeStateProgressBar(true);
        String url = "https://api.jikan.moe/v4" + query;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    List<Anime> animeListJson = new ArrayList<>();
                    for (int i = 0; i < dataArray.length(); i++) {

                        JSONObject animeObject = dataArray.getJSONObject(i);
                        long id = animeObject.getLong("mal_id");
                        String title = animeObject.getString("title");
                        String imageUrl = animeObject.getJSONObject("images").getJSONObject("jpg").getString("image_url");
                        String description = animeObject.getString("synopsis");

                        Anime anime = new Anime();
                        anime.setId(id);
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

    public void setupRecyclerView() {
        newsAnimeRv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsRecyclerAdapter(animeList);
        newsAnimeRv.setAdapter(adapter);
    }

    public void updateRecyclerView(List<Anime> animeL) {
        this.animeList.clear();
        this.animeList.addAll(animeL);
    }

    public void userSearching() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String url = "/anime?q=" + query;
                getAnimes(url);
                setTitleRelatedToNews(query);
                searchView.setQuery("",false);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void setTitleRelatedToNews(String text){
        String firstLetter = text.substring(0, 1).toUpperCase();
        String restOfText = text.substring(1).toLowerCase();
        String capitalizedText = firstLetter + restOfText;
        titleRelatedToNews.setText(capitalizedText);
    }
}