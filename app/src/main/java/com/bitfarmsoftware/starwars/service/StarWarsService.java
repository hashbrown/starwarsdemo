package com.bitfarmsoftware.starwars.service;

import android.net.Uri;

import com.bitfarmsoftware.starwars.model.Film;
import com.bitfarmsoftware.starwars.model.Starship;
import com.bitfarmsoftware.starwars.service.remote.StarWarsApi;

import javax.inject.Inject;

import retrofit.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author Brad Armstrong, Bitfarm Software
 */
public class StarWarsService {

    private final StarWarsApi api;

    @Inject
    public StarWarsService(StarWarsApi remoteApi){
        this.api = remoteApi;
    }


   public  Observable<Film> getFilmAppearances(Starship starship){

       return Observable.from(starship.getFilms())
               .flatMap(new Func1<Uri, Observable<Response<Film>>>() {
                   @Override
                   public Observable<Response<Film>> call(final Uri uri) {
                       return api.getFilmByUrl(uri.toString());
                   }
               }).flatMap(new Func1<Response<Film>, Observable<Film>>() {
                   @Override
                   public Observable<Film> call(final Response<Film> response) {
                       return Observable.just(response.body());
                   }
       });
    }
}
