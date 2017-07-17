package br.com.dioni.pipoteca.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.dioni.pipoteca.R;
import br.com.dioni.pipoteca.adapter.FavoriteAdapter;
import br.com.dioni.pipoteca.models.Movie;
import io.realm.Realm;
import io.realm.RealmResults;


public class FavoritesFragment extends Fragment {

    private RecyclerView mRecyclerFavorite;
    private Realm mRealm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        mRecyclerFavorite = (RecyclerView) view.findViewById(R.id.recycle_all_favorites);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerFavorite.setLayoutManager(layoutManager);

        mRealm = Realm.getDefaultInstance();
        RealmResults<Movie> mResults = mRealm.where(Movie.class).findAll();

        mRecyclerFavorite.setAdapter(new FavoriteAdapter(getContext(), mResults));

        return view;
    }

}
