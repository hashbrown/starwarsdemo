package com.bitfarmsoftware.starwars.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitfarmsoftware.starwars.R;
import com.bitfarmsoftware.starwars.StarWarsApplication;
import com.bitfarmsoftware.starwars.model.PagedApiResponse;
import com.bitfarmsoftware.starwars.model.Starship;
import com.bitfarmsoftware.starwars.service.remote.StarWarsApi;

import java.util.List;

import javax.inject.Inject;

import retrofit.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.bitfarmsoftware.starwars.ui.StarshipsListFragment.OnStarshipSelectedListener} interface
 * to handle interaction events.
 * Use the {@link StarshipsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StarshipsListFragment extends Fragment {

    private OnStarshipSelectedListener mListener;

    private StarshipListViewAdapter adapter;

    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Inject
    StarWarsApi api;

    @Inject
    SharedPreferences prefs;

    public StarshipsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment StarshipsListFragment.
     */
    public static StarshipsListFragment newInstance() {
        StarshipsListFragment fragment = new StarshipsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((StarWarsApplication)getActivity().getApplication()).getApplicationComponent().inject(this);

        adapter = new StarshipListViewAdapter(this.getActivity(), new StarshipListViewAdapter.StarshipClickListener() {
            @Override
            public void onClicked(final Starship starship) {
                mListener.onSelected(starship);
            }
        });
        loadStarships();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if(context instanceof OnStarshipSelectedListener){
            mListener = (OnStarshipSelectedListener) context;
        } else {
            throw new RuntimeException("Fragment context is not a OnStarshipSelectedListener");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_starship_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recList = (RecyclerView) view.findViewById(R.id.starship_list);
        GridLayoutManager layoutMgr = new GridLayoutManager(this.getContext(), 2);
        recList.setLayoutManager(layoutMgr);
        recList.setAdapter(adapter);

    }

    private void loadStarships(){
        subscriptions.add(api.getStarships()
                .map(new Func1<Response<PagedApiResponse<Starship>>, List<Starship>>() {
                    @Override
                    public List<Starship> call(final Response<PagedApiResponse<Starship>> response) {
                        return response.body().getResults();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Starship>>() {
                               @Override
                               public void call(final List<Starship> starships) {
                                   adapter.addStarships(starships);
                               }
                           }
                ));
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStarshipSelectedListener {
        void onSelected(Starship starship);
    }
}
