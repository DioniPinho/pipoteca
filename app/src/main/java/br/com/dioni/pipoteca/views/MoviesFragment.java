package br.com.dioni.pipoteca.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.dioni.pipoteca.R;
import br.com.dioni.pipoteca.adapter.MovieAdapter;
import br.com.dioni.pipoteca.constants.MovieConstants;
import br.com.dioni.pipoteca.listeners.OnMovieListenerInteraction;
import br.com.dioni.pipoteca.models.Movie;
import br.com.dioni.pipoteca.models.MovieCatalog;
import br.com.dioni.pipoteca.services.MovieApi;
import br.com.dioni.pipoteca.services.MovieService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MoviesFragment extends Fragment {

    private static final String TAG = "MovieFragment";
    private RecyclerView mRecyclerView;
    private Retrofit retrofit;
    private MovieAdapter mMovieAdpter;
    private int nextPage;
    private boolean readyToLoad;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_movies, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_all_films);
        mRecyclerView.setHasFixedSize(true);


        OnMovieListenerInteraction onMovieListener = new OnMovieListenerInteraction() {
            @Override
            public void onDetails(Movie movie) {

                startActivity(new Intent(getContext(), MovieDetailsActivity.class)
                        .putExtra(MovieConstants.MOVIE_BUNDLE_ID, movie));


            }
        };


        mMovieAdpter = new MovieAdapter(getContext(), onMovieListener);
        mRecyclerView.setAdapter(mMovieAdpter);

        final GridLayoutManager gridManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(gridManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int visibleItemCount = gridManager.getChildCount();
                    int totalItemCount = gridManager.getItemCount();
                    int pastVisibleItems = gridManager.findFirstVisibleItemPosition();

                    if (readyToLoad) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {

                            nextPage += 1;
                            getMovieData(nextPage);

                        }
                    }
                }
            }
        });

        retrofit = MovieApi.getRetrofit();
        readyToLoad = true;
        nextPage = 1;
        getMovieData(nextPage);
        return view;
    }

    private void getMovieData(final int nextPage) {
        MovieService movieService = retrofit.create(MovieService.class);
        Call<MovieCatalog> requestMovieData = movieService
                .getMovieDataService(MovieConstants.MOVIE_API_KEY, getString(R.string.language),
                        nextPage, false);


        requestMovieData.enqueue(new Callback<MovieCatalog>() {
            @Override
            public void onResponse(Call<MovieCatalog> call, Response<MovieCatalog> response) {
                if (response.isSuccessful()) {
                    MovieCatalog movieCatalog = response.body();
                    List<Movie> movies = movieCatalog.getResults();
                    mMovieAdpter.addMovieList(movies);
                } else {
                    Log.e(TAG, "onResponse(): " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<MovieCatalog> call, Throwable t) {
                Log.e(TAG, "onFailure(): " + t.getMessage());
                readyToLoad = false;

            }
        });
    }

}
