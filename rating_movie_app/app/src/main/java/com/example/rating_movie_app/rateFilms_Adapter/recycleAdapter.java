package com.example.rating_movie_app.rateFilms_Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rating_movie_app.R;

import java.util.ArrayList;

public class recycleAdapter extends RecyclerView.Adapter<recycleAdapter.ViewHolder> {

    public interface ItemClickListener {
        void onItemClick(String movieName);
    }
    ArrayList<recycleDomain> ratingList;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public recycleAdapter(ArrayList<recycleDomain> ratingList, ItemClickListener itemClickListener) {
        this.ratingList = ratingList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public recycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.rated_film_elem, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull recycleAdapter.ViewHolder holder, int position) {
        String title = ratingList.get(position).getTitle();
        holder.itemTitle.setText(title);
        double rating = ratingList.get(position).getRating();
        holder.itemRating.setText(String.valueOf(rating));
        if(rating < 5){
            holder.itemLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.backg_red));
        }
        if(rating >= 5 && rating <7){
            holder.itemLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.backg_yellow));
        }
        if(rating >= 7){
            holder.itemLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.backg_green));
        }
        holder.itemYear.setText(String.valueOf(ratingList.get(position).getYear()));
        holder.itemDateRate.setText(ratingList.get(position).getDateRate());
        holder.itemGenres.setText(ratingList.get(position).getGenres());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(title);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ratingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        TextView itemGenres;
        TextView itemYear;
        TextView itemRating;
        TextView itemDateRate;
        ConstraintLayout itemLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemTitle = itemView.findViewById(R.id.textTitle);
            itemGenres = itemView.findViewById(R.id.textGenres);
            itemYear = itemView.findViewById(R.id.textYear);
            itemRating = itemView.findViewById(R.id.textRating);
            itemDateRate = itemView.findViewById(R.id.textDateRate);
            itemLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
