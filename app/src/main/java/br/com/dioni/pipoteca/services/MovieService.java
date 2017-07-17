package br.com.dioni.pipoteca.services;


import android.renderscript.Sampler;

import java.util.List;

import br.com.dioni.pipoteca.models.Movie;
import br.com.dioni.pipoteca.models.MovieCatalog;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("movie")
    Call<MovieCatalog> getMovieDataService(@Query("api_key") String apiKey,
                                           @Query("language") String language,
                                           @Query("page") int page,
                                           @Query("include_adult") boolean adult);
}
