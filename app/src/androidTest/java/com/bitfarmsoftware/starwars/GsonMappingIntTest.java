package com.bitfarmsoftware.starwars;

import android.net.Uri;
import android.sax.StartElementListener;
import android.support.test.runner.AndroidJUnit4;

import com.bitfarmsoftware.starwars.model.PagedApiResponse;
import com.bitfarmsoftware.starwars.model.Starship;
import com.bitfarmsoftware.starwars.model.UriTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Brad Armstrong, Bitfarm Software
 */
@RunWith(AndroidJUnit4.class)
public class GsonMappingIntTest {

    private static Gson gson;

    @BeforeClass
    public static void setupClass(){
        gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriTypeAdapter())
                .create();
    }

    @Test
    public void shouldMapPagedApiResponse() throws IOException{
        InputStream response = getContext().getAssets().open("starships.json");

        PagedApiResponse<Starship> starships = gson.fromJson(new JsonReader(new InputStreamReader(response)), PagedApiResponse.class);
        assertEquals(37, starships.getCount());
        assertEquals("http://swapi.co/api/starships/?page=2", starships.getNext().toString());
        assertNull(starships.getPrevious());
    }

    @Test
    public void shouldMapStarships() throws IOException{
        InputStream response = getContext().getAssets().open("starships.json");

        TypeToken<PagedApiResponse<Starship>> responseType = new TypeToken<PagedApiResponse<Starship>>(){};
        PagedApiResponse<Starship> pagedResponse = gson.fromJson(new JsonReader(new InputStreamReader(response)), responseType.getType());
        List<Starship> starships = pagedResponse.getResults();
        assertNotNull(starships);
        assertEquals(10, starships.size());

        Starship ship = starships.get(2);
        assertEquals("http://swapi.co/api/starships/10/", ship.getId().toString());
        assertEquals("Millennium Falcon", ship.getName());

        List<Uri> films = ship.getFilms();
        assertNotNull(films);
        assertEquals(4, films.size());
        assertEquals("http://swapi.co/api/films/7/", films.get(0).toString());

        List<Uri> pilots = ship.getPilots();
        assertNotNull(pilots);
        assertEquals(4, pilots.size());
        assertEquals("http://swapi.co/api/people/13/", pilots.get(0).toString());

    }
}
