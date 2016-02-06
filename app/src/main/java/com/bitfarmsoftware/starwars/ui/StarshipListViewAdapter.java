package com.bitfarmsoftware.starwars.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitfarmsoftware.starwars.R;
import com.bitfarmsoftware.starwars.model.Starship;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Brad Armstrong, Bitfarm Software
 */
public class StarshipListViewAdapter extends RecyclerView.Adapter<StarshipListViewAdapter.StarshipViewHolder> {

    public static interface StarshipClickListener {
        public void onClicked(Starship starship);
    }

    final private Context ctx;
    final private StarshipClickListener clickListener;
    private Map<String,Uri> starshipImages = new HashMap<>(16);
    private List<Starship> starships = new ArrayList<>(16);

    public StarshipListViewAdapter(Context context,StarshipClickListener clickListener){
        this.ctx = context;
        this.clickListener = clickListener;
        loadImageMap();

    }

    public void addStarships(List<Starship> starships){
        int insertPos = this.starships.isEmpty()?0:this.starships.size();
        this.starships.addAll(starships);
        this.notifyItemRangeInserted(insertPos, starships.size());
    }

    @Override
    public StarshipViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View cardView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_starship, parent, false);
        return new StarshipViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(final StarshipViewHolder holder, final int position) {

        final Starship ship = this.starships.get(position);
        if (ship != null) {
            holder.name.setText(ship.getName());
            Uri image = starshipImages.get(ship.getName());
            Picasso.with(ctx)
                    .load(image)
                    .fit()
                    .centerCrop()
                    .into(holder.image);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    clickListener.onClicked(ship);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return starships.size();
    }

    public static class StarshipViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name;

        public StarshipViewHolder(final View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.starship_image);
            name = (TextView) itemView.findViewById(R.id.starship_name);
        }
    }

    private void loadImageMap(){
        starshipImages.put("Executor", Uri.parse("http://img.lum.dolimg.com/v1/images/databank_executor_01_169_8157df82.jpeg?region=57%2C0%2C1503%2C845"));
        starshipImages.put("Imperial shuttle",Uri.parse("http://img.lum.dolimg.com/v1/images/veh_ia_1752_040381b2.jpeg?region=100%2C0%2C983%2C981&width=480"));
        starshipImages.put( "Millennium Falcon",Uri.parse("http://img.lum.dolimg.com/v1/images/Millennium-Falcon_018ea796.jpeg?region=0%2C1%2C1536%2C864"));
        starshipImages.put("Death Star",Uri.parse("http://img.lum.dolimg.com/v1/images/Death-Star-I-copy_36ad2500.jpeg?region=0%2C0%2C1600%2C900&width=1536"));
        starshipImages.put("Sentinel-class landing craft",Uri.parse("http://img.lum.dolimg.com/v1/images/Imperial-Sentinel-Class-Shuttle_a2dc7d3b.jpeg?region=0%2C0%2C1596%2C898&width=1536"));
        starshipImages.put("Y-wing",Uri.parse("http://img.lum.dolimg.com/v1/images/Y-Wing-Fighter_0e78c9ae.jpeg?region=0%2C0%2C1536%2C864"));
        starshipImages.put("X-wing",Uri.parse("http://img.lum.dolimg.com/v1/images/X-Wing-Fighter_47c7c342.jpeg?region=0%2C1%2C1536%2C864"));
        starshipImages.put("TIE Advanced x1",Uri.parse("http://img.lum.dolimg.com/v1/images/TIE-Fighter_25397c64.jpeg?region=0%2C1%2C2048%2C1152&width=1536"));
        starshipImages.put("Slave 1",Uri.parse("http://img.lum.dolimg.com/v1/images/databank_slavei_01_169_8dc3102d.jpeg?region=0%2C0%2C1560%2C878&width=1536"));
        starshipImages.put("EF76 Nebulon-B escort frigate",Uri.parse("http://vignette2.wikia.nocookie.net/starwars/images/5/50/NBfrigate.JPG/revision/latest?cb=20061215024715"));
    }
}
