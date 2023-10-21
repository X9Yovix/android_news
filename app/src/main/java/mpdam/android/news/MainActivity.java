package mpdam.android.news;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String latestAnimes = "https://api.jikan.moe/v4/seasons/now";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, latestAnimes, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray dataArray = response.getJSONArray("data");

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject animeObject = dataArray.getJSONObject(i);
                        String title = animeObject.getString("title");
                        String imageUrl = animeObject.getJSONObject("images").getJSONObject("jpg").getString("image_url");


                        List<Anime> animeList = new ArrayList<>();
                        Anime anime = new Anime();
                        anime.setTitle(title);
                        anime.setImageUrl(imageUrl);

                        animeList.add(anime);
                        System.out.println(anime);
                    }
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
}