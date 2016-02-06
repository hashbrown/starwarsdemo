package com.bitfarmsoftware.starwars.service.remote;

import com.bitfarmsoftware.starwars.model.Film;
import com.bitfarmsoftware.starwars.model.PagedApiResponse;
import com.bitfarmsoftware.starwars.model.Starship;

import retrofit.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Url;
import rx.Observable;

/**
 * @author Brad Armstrong, Bitfarm Software
 */
public interface StarWarsApi {

    @GET("starships")
    Observable<Response<PagedApiResponse<Starship>>> getStarships();

    @GET
    Observable<Response<Film>> getFilmByUrl(@Url String filmUrl);
}
