package com.bitfarmsoftware.starwars.di;

import com.bitfarmsoftware.starwars.ui.StarshipDetailFragment;
import com.bitfarmsoftware.starwars.ui.StarshipsListFragment;

/**
 * @author Brad Armstrong, Bitfarm Software
 */
public interface StarWarsComponent {
    void inject(StarshipsListFragment fragment);
    void inject(StarshipDetailFragment fragment);
}
