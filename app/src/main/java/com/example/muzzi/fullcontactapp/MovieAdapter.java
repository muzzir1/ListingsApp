package com.example.muzzi.fullcontactapp;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder>
{
    private List<Movie> movieList;

    public class MyViewHolder extends ViewHolder
    {
        private TextView Title, Year, Rating, Plot;
        private ImageView MoviePoster;

        public MyViewHolder(View v)
        {
            super(v);
            Title = (TextView)v.findViewById(R.id.titleView);
            MoviePoster = (ImageView) v.findViewById(R.id.posterimageView);
            Year = (TextView) v.findViewById(R.id.yearview);
            Rating = (TextView) v.findViewById(R.id.ratingview);
            Plot = (TextView) v.findViewById(R.id.plotview);


        }

    }

    public MovieAdapter(List<Movie> mlist)
    {
        this.movieList = mlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup vGroup, int i)
    {
        View itemview = LayoutInflater.from(vGroup.getContext()).inflate(R.layout.movie_row_layout, vGroup,false);

        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i)
    {
        Movie mve = movieList.get(i);
        myViewHolder.Title.setText(mve.getTitle());
        myViewHolder.Year.setText(mve.getYear());
        myViewHolder.Rating.setText(mve.getRating());
        myViewHolder.Plot.setText(mve.getPlot());

        Uri uri = Uri.parse(mve.getPosterimage());
        Context context = myViewHolder.MoviePoster.getContext();
        Picasso.with(context).load(uri).into(myViewHolder.MoviePoster);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


}
