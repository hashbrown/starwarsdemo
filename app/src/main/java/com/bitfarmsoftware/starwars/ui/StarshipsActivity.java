package com.bitfarmsoftware.starwars.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bitfarmsoftware.starwars.R;
import com.bitfarmsoftware.starwars.model.Starship;
import com.squareup.picasso.Picasso;

public class StarshipsActivity extends AppCompatActivity implements StarshipsListFragment.OnStarshipSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starships);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        loadHeader();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frag_container, StarshipsListFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onSelected(final Starship starship) {

        StarshipDetailFragment detail = StarshipDetailFragment.newInstance(starship);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_container, detail)
                .addToBackStack(StarshipDetailFragment.class.getName())
                .commit();

    }

    private void loadHeader(){
        final ImageView headerImage = (ImageView) findViewById(R.id.header_image);
        Picasso.with(this)
                .load(R.drawable.star_wars_logo)
                .fit()
                .centerCrop()
                .into(headerImage);
    }


}
