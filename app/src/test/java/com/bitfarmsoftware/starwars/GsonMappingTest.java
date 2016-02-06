package com.bitfarmsoftware.starwars;

import android.net.Uri;

import com.bitfarmsoftware.starwars.model.PagedApiResponse;
import com.bitfarmsoftware.starwars.model.Starship;
import com.bitfarmsoftware.starwars.model.UriTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

/**
 * @author Brad Armstrong, Bitfarm Software
 */
public class GsonMappingTest {

    private static Gson gson;

    @BeforeClass
    public static void setupClass(){
        gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriTypeAdapter())
                .create();
    }

    @Test
    public void shouldMapPagedApiResponse() throws IOException {
        InputStream response = GsonMappingTest.class.getResourceAsStream("/starships.json");

        PagedApiResponse<Starship> starships = gson.fromJson(new JsonReader(new InputStreamReader(response)), PagedApiResponse.class);
        assertEquals(37, starships.getCount());
        // Uri mock values always null in unit tests
//        assertEquals("http://swapi.co/api/starships/?page=2", starships.getNext().toString());
    }

    @Test
    public void shouldMapStarships() throws IOException {
        InputStream response = GsonMappingTest.class.getResourceAsStream("/starships.json");

        TypeToken<PagedApiResponse<Starship>> responseType = new TypeToken<PagedApiResponse<Starship>>(){};
        PagedApiResponse<Starship> starships = gson.fromJson(new JsonReader(new InputStreamReader(response)), responseType.getType());
        Starship ss = starships.results.get(0);
        assertEquals("Sentinel-class landing craft", ss.getName());
        assertEquals("Sentinel-class landing craft", ss.getModel());
        assertEquals("Sienar Fleet Systems, Cyngus Spaceworks", ss.getManufacturer());
        assertEquals("240000", ss.getCost());
        assertEquals("38", ss.getLength());
        assertEquals("1000", ss.getMaxAtmospheringSpeed());
        assertEquals("5", ss.getCrew());
        assertEquals("75", ss.getPassengers());
        assertEquals("180000", ss.getCargoCapacity());
        assertEquals("1.0", ss.getHyperdriveRating());
        assertEquals("70", ss.getMegalights());
        assertEquals("landing craft", ss.getStarshipClass());
    }
}
