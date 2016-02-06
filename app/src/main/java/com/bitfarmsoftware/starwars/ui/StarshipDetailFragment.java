package com.bitfarmsoftware.starwars.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitfarmsoftware.starwars.R;
import com.bitfarmsoftware.starwars.StarWarsApplication;
import com.bitfarmsoftware.starwars.model.Film;
import com.bitfarmsoftware.starwars.model.PagedApiResponse;
import com.bitfarmsoftware.starwars.model.Starship;
import com.bitfarmsoftware.starwars.service.StarWarsService;
import com.bitfarmsoftware.starwars.service.remote.StarWarsApi;

import java.util.List;

import javax.inject.Inject;

import retrofit.Response;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class StarshipDetailFragment extends Fragment {

    public static final String SAVED_STATE_STARSHIP = "starship";

    @Inject
    StarWarsService service;

    private StarshipDetailAdapter adapter;

    private Starship starship;

    private CompositeSubscription subscriptions = new CompositeSubscription();

    public StarshipDetailFragment() {
        // Required empty public constructor
    }

    public static StarshipDetailFragment newInstance(Starship starship) {
        StarshipDetailFragment fragment = new StarshipDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(SAVED_STATE_STARSHIP, starship);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((StarWarsApplication) getActivity().getApplication()).getApplicationComponent().inject(this);

        if(savedInstanceState != null){
            starship = (Starship) savedInstanceState.getSerializable(SAVED_STATE_STARSHIP);
        } else {
            starship = (Starship) getArguments().getSerializable(SAVED_STATE_STARSHIP);
        }
        adapter = new StarshipDetailAdapter(this.getActivity(), starship);
        loadFilms();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_starship_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recList = (RecyclerView) view.findViewById(R.id.starship_detail);
        LinearLayoutManager layoutMgr = new LinearLayoutManager(this.getContext());
        recList.setLayoutManager(layoutMgr);
        recList.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("starship", starship);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }

    private void loadFilms() {
        subscriptions.add(service.getFilmAppearances(this.starship)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Film>>() {
                               @Override
                               public void call(final List<Film> films) {
                                   adapter.setFilms(films);
                                   Timber.d("FILMS LOADED");
                               }
                           }
                ));
        Timber.d("LOADING FILMS");
    }

}
