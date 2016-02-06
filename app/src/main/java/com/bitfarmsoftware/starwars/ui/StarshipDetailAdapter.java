package com.bitfarmsoftware.starwars.ui;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitfarmsoftware.starwars.R;
import com.bitfarmsoftware.starwars.model.Film;
import com.bitfarmsoftware.starwars.model.Starship;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Brad Armstrong, Bitfarm Software
 */
public class StarshipDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SHIP_DETAIL = 0;
    private static final int TYPE_FILM = 1;
    private static final int TYPE_HEADER = 2;

    private Context ctx;
    private Starship starship;
    private List<Film> films = new ArrayList<>(6);

    public StarshipDetailAdapter(Context context, Starship starship){
        this.ctx = context;
        this.starship = starship;
    }

    public void setFilms(List<Film> films){
        this.films.addAll(films);
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        switch(viewType){
            case TYPE_SHIP_DETAIL:
                View detailView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.card_starship_detail, parent, false);
                return new StarshipDetailView(detailView);
            case TYPE_FILM:
                View filmView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.card_starship_film, parent, false);
                return new FilmView(filmView);
            case TYPE_HEADER:
                View headerView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.card_starship_header, parent, false);
                return new HeaderView(headerView);

        }

        return null;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof StarshipDetailView){
            ((StarshipDetailView) holder).model.setText(starship.getModel());
            ((StarshipDetailView) holder).mfr.setText(starship.getManufacturer());
            ((StarshipDetailView) holder).cost.setText(starship.getCost());
            ((StarshipDetailView) holder).hyperdriveRating.setText(starship.getHyperdriveRating());
            ((StarshipDetailView) holder).shipClass.setText(starship.getStarshipClass());
        } else if (holder instanceof FilmView){
            Film film = this.films.get(position - 3);
            StringBuilder title = new StringBuilder("Episode ")
                    .append(film.getEpisode())
                    .append(": ")
                    .append(film.getTitle());
            ((FilmView) holder).title.setText(title.toString());
            ((FilmView) holder).director.setText(film.getDirector());
            ((FilmView) holder).producer.setText(film.getProducer());
            ((FilmView) holder).releaseDate.setText(film.getReleaseDateString());
        } else if (holder instanceof HeaderView){
            String header = null;
            if(position == 0){
                header = starship.getName() + " Details";
            } else {
                header = "Film Appearances";
            }
            SpannableString spanString = new SpannableString(header);
            spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
            ((HeaderView) holder).title.setText(spanString);
        }

    }

    @Override
    public int getItemCount() {
        return films.size() + 3;
    }

    @Override
    public int getItemViewType(final int position) {
        switch (position){
            case 0:
            case 2:
                return TYPE_HEADER;
            case 1:
                return TYPE_SHIP_DETAIL;
            default:
                return TYPE_FILM;
        }

    }

    public static class StarshipDetailView extends RecyclerView.ViewHolder {

        TextView model;
        TextView mfr;
        TextView cost;
        TextView hyperdriveRating;
        TextView shipClass;

        public StarshipDetailView(final View itemView) {
            super(itemView);

            model = (TextView) itemView.findViewById(R.id.ship_detail_model);
            mfr = (TextView) itemView.findViewById(R.id.ship_detail_mfr);
            cost = (TextView) itemView.findViewById(R.id.ship_detail_cost);
            hyperdriveRating = (TextView) itemView.findViewById(R.id.ship_detail_hyperdrive);
            shipClass = (TextView) itemView.findViewById(R.id.ship_detail_class);
        }
    }

    public static class FilmView extends RecyclerView.ViewHolder {

        TextView title;
        TextView director;
        TextView producer;
        TextView releaseDate;

        public FilmView(final View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.film_title);
            director = (TextView) itemView.findViewById(R.id.film_director);
            producer = (TextView) itemView.findViewById(R.id.film_producer);
            releaseDate = (TextView) itemView.findViewById(R.id.film_release_date);
        }
    }

    public static class HeaderView extends RecyclerView.ViewHolder {

        TextView title;

        public HeaderView(final View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.header_name);
        }
    }
}
