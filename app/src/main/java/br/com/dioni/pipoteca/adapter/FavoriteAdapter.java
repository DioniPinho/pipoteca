package br.com.dioni.pipoteca.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import br.com.dioni.pipoteca.R;
import br.com.dioni.pipoteca.models.Movie;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private final Context mContext;
    private RealmResults<Movie> mMovieResults;

    public FavoriteAdapter(Context context, RealmResults<Movie> mResults) {
        this.mContext = context;
        this.mMovieResults = mResults.sort("title");

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_recycler_all_favorites, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Movie movie = mMovieResults.get(position);
        holder.mFavoriteTitle.setText(movie.getTitle());

        Glide.with(mContext)
                .load("http://image.tmdb.org/t/p/w185" + movie.getPosterPath())
                .apply(RequestOptions.centerCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.mFavoriteImage);


        holder.mFavoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(mContext)
                    .setTitle(R.string.alert)
                    .setMessage(R.string.you_sure_to_remove)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Snackbar.make(view, R.string.movie_removed, BaseTransientBottomBar.LENGTH_LONG)
                                    .show();


                            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    movie.deleteFromRealm();

                                }
                            });

                        notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();

            }
        });
    }


    @Override
    public int getItemCount() {
        return mMovieResults.size();


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mFavoriteTitle;
        private ImageView mFavoriteImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mFavoriteImage = (ImageView) itemView.findViewById(R.id.iv_favorite_movie_picture);
            mFavoriteTitle = (TextView) itemView.findViewById(R.id.text_favorite_movie_title);
        }
    }
}
