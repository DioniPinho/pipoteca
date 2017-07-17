package br.com.dioni.pipoteca.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import br.com.dioni.pipoteca.R;
import br.com.dioni.pipoteca.constants.MovieConstants;
import br.com.dioni.pipoteca.models.Movie;
import io.realm.Realm;
import io.realm.RealmResults;

public class MovieDetailsActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView mTextTitle;
    private TextView mTextSinopse;
    private ImageView mIvPoster;
    private ImageButton mIbFavorite;
    private Movie mMovie;
    private Realm realm;
    private boolean favorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.movie_details);


        mTextTitle = (TextView) findViewById(R.id.text_details_movie_title);
        mTextSinopse = (TextView) findViewById(R.id.text_details_movie_sinopse);
        mIvPoster = (ImageView) findViewById(R.id.iv_details_movies_picture);
        mIbFavorite = (ImageButton) findViewById(R.id.ib_details_movie_favorite);

        mIbFavorite.setOnClickListener(this);

        Intent intent = getIntent();


        if (intent != null) {
            mMovie = (Movie) intent.getSerializableExtra(MovieConstants.MOVIE_BUNDLE_ID);

            this.mTextTitle.setText(mMovie.getTitle());
            this.mTextSinopse.setText(mMovie.getSinopse());

            String poster = mMovie.getBackdropPath();

            Glide.with(this)
                    .load(MovieConstants.URL_BASE_IMAGE_W500 + poster)
                    .apply(RequestOptions.centerCropTransform()
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mIvPoster);
        }

        checkIfFavorited();
        checkFavorite();

    }

    @Override
    public void onClick(final View view) {
        if (view.getId() == R.id.ib_details_movie_favorite) {
            this.realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Movie movieRealm = realm.copyToRealm(mMovie);

                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Snackbar.make(view.getRootView(), R.string.movie_success,
                            Snackbar.LENGTH_SHORT).show();
                    favorite = true;
                    checkFavorite();
                }
            });

            checkFavorite();
        }
    }

    private void checkFavorite() {
        if (favorite) {
            this.mIbFavorite.setImageResource(R.drawable.ic_heart_favorited);
        } else {
            this.mIbFavorite.setImageResource(R.drawable.ic_heart_not_favorited);

        }
    }

    private void checkIfFavorited() {
        this.realm = Realm.getDefaultInstance();
        RealmResults<Movie> realmResults = this.realm.where(Movie.class)
                .equalTo("id", mMovie.getId())
                .findAll();


        if (realmResults.size() > 0 && !realmResults.isEmpty()) {
            this.favorite = true;
        } else {
            this.favorite = false;
        }
    }
}
