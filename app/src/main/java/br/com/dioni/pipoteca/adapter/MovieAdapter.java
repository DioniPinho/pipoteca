package br.com.dioni.pipoteca.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import br.com.dioni.pipoteca.R;
import br.com.dioni.pipoteca.constants.MovieConstants;
import br.com.dioni.pipoteca.listeners.OnMovieListenerInteraction;
import br.com.dioni.pipoteca.models.Movie;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {


    private List<Movie> dataMovies;
    private Context context;
    private OnMovieListenerInteraction mOnMovieListener;

    public MovieAdapter(Context context, OnMovieListenerInteraction onMovieListener) {
        this.context = context;
        this.dataMovies = new ArrayList<>();
        this.mOnMovieListener = onMovieListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_recycler_all_movies, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = dataMovies.get(position);
        holder.mTexMovieTitle.setText(movie.getTitle());

        Glide.with(context)
                .load(MovieConstants.URL_BASE_IMAGE_W185 +movie.getPosterPath())
                .apply(RequestOptions.centerCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.mIvMoviesPicture);
        holder.bindDataClick(movie, mOnMovieListener);

    }

    @Override
    public int getItemCount() {
        return this.dataMovies.size();
    }

    public void addMovieList(List<Movie> movies) {
        dataMovies.addAll(movies);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTexMovieTitle;
        private ImageView mIvMoviesPicture;


        public ViewHolder(View itemView) {
            super(itemView);

            mTexMovieTitle = (TextView) itemView.findViewById(R.id.text_all_movies_title);
            mIvMoviesPicture = (ImageView) itemView.findViewById(R.id.iv_all_movies_picture);
        }

        public void bindDataClick(final Movie movie, final OnMovieListenerInteraction movieListener) {
            this.mIvMoviesPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieListener.onDetails(movie);
                }
            });
        }
    }
}
