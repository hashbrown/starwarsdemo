package com.bitfarmsoftware.starwars.service.remote;

import android.net.Uri;

import com.bitfarmsoftware.starwars.model.Film;
import com.bitfarmsoftware.starwars.model.Starship;
import com.bitfarmsoftware.starwars.service.StarWarsService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import retrofit.Response;
import rx.Observable;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Brad Armstrong, Bitfarm Software
 */
public class StarWarsServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private StarWarsApi api;
    private StarWarsService service;

    @Before
    public void setup(){
        service = new StarWarsService(api);
    }


    @Test
    public void shouldReturnAllFilmsForStarship(){
        // Setup Mock Models
        Film film1 = mock(Film.class);
        when(film1.getTitle()).thenReturn("Star Wars XV: But Who Was Yoda's Father?");
        Film film2 = mock(Film.class);
        when(film2.getTitle()).thenReturn("Star Wars XX: I Can't Believe They built Another Death Star");

        Uri film1Uri = mock(Uri.class);
        when(film1Uri.toString()).thenReturn("http://films/1");

        Uri film2Uri = mock(Uri.class);
        when(film2Uri.toString()).thenReturn("http://films/2");

        List<Uri> filmUris = new ArrayList<>();
        filmUris.add(film1Uri);
        filmUris.add(film2Uri);


        Starship starship = mock(Starship.class);
        when(starship.getFilms()).thenReturn(filmUris);

        // Mock api responses
        Observable<Response<Film>> film1Response = Observable.just(Response.success(film1));
        Observable<Response<Film>> film2Response = Observable.just(Response.success(film2));

        when(api.getFilmByUrl(film1Uri.toString())).thenReturn(film1Response);
        when(api.getFilmByUrl(film2Uri.toString())).thenReturn(film2Response);

        // execute the test
        Observable<Film> starshipFilms = service.getFilmAppearances(starship);
        TestSubscriber<Film> subscriber = new TestSubscriber<>();
        starshipFilms.subscribe(subscriber);

        // asset results
        subscriber.assertCompleted();
        subscriber.assertValueCount(2);
        subscriber.assertValues(film1, film2);

        List<Film> filmsObserved = subscriber.getOnNextEvents();
        assertEquals(film1.getTitle(), filmsObserved.get(0).getTitle());
        assertEquals(film2.getTitle(), filmsObserved.get(1).getTitle());


    }

    @Test
    public void shouldNotCallApiUntilSubscribed(){
        Uri film1Uri = mock(Uri.class);
        when(film1Uri.toString()).thenReturn("http://films/1");

        Uri film2Uri = mock(Uri.class);
        when(film2Uri.toString()).thenReturn("http://films/2");

        List<Uri> filmUris = new ArrayList<>();
        filmUris.add(film1Uri);
        filmUris.add(film2Uri);

        Starship starship = mock(Starship.class);
        when(starship.getFilms()).thenReturn(filmUris);

        // execute the test
        Observable<Film> starshipFilms = service.getFilmAppearances(starship);
        TestSubscriber<Film> subscriber = new TestSubscriber<>();

        // verify api not yet called
        verify(api, never()).getFilmByUrl(isA(String.class));

        starshipFilms.subscribe(subscriber);

        // verify api called for each film
        verify(api, times(2)).getFilmByUrl(isA(String.class));

    }

}
