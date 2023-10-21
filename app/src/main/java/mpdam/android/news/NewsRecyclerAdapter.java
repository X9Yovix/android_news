package mpdam.android.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {
    private List<Anime> animeList;
    public NewsRecyclerAdapter(List<Anime> animeList){
        this.animeList=animeList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_row,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Anime anime = this.animeList.get(position);
        Picasso.get().load(anime.getImageUrl())
                .error(R.drawable.baseline_image_not_supported_24)
                .placeholder(R.drawable.baseline_image_not_supported_24)
                .into(holder.articleImage);
        holder.articleTitle.setText(anime.getTitle());
        holder.articleDescription.setText(anime.getDescription());
    }

    @Override
    public int getItemCount() {
        return this.animeList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{

         private ImageView articleImage;
         private TextView articleTitle;
         private TextView articleDescription;

         public NewsViewHolder(@NonNull View itemView) {
             super(itemView);

             articleImage = itemView.findViewById(R.id.articleImageIV);
             articleTitle = itemView.findViewById(R.id.articleTitleTV);
             articleDescription = itemView.findViewById(R.id.articleDescriptionTV);
         }
     }
}
