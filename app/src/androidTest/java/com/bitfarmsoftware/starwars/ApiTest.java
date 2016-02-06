package com.bitfarmsoftware.starwars;


import com.bitfarmsoftware.starwars.model.PagedApiResponse;
import com.bitfarmsoftware.starwars.model.Starship;
import com.bitfarmsoftware.starwars.model.UriTypeAdapter;
import com.bitfarmsoftware.starwars.service.remote.StarWarsApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.observers.TestSubscriber;
import timber.log.Timber;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * A smoke test that executes the live api
 */
@RunWith(AndroidJUnit4.class)
public class ApiTest{

    static StarWarsApi api;

    @BeforeClass
    public static void setupClass(){
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new LoggingInterceptor());

//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(Uri.class, new UriTypeAdapter())
//                .create();
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://swapi.co/api/")
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api = retrofit.create(StarWarsApi.class);
    }

    @Test
    public void shouldRetrieveFirstPageOfStarships(){
        final Observable<Response<PagedApiResponse<Starship>>> starships = api.getStarships();
        TestSubscriber<Response<PagedApiResponse<Starship>>> subscriber = new TestSubscriber<>();
        starships.subscribe(subscriber);

        if(!subscriber.getOnErrorEvents().isEmpty())
            fail("Unexpected Error:" + subscriber.getOnErrorEvents().get(0).getMessage());

        subscriber.assertCompleted();
        subscriber.assertValueCount(1);

        final Response<PagedApiResponse<Starship>> wrappedResponse = subscriber.getOnNextEvents().get(0);
        assertTrue(wrappedResponse.isSuccess());
        final PagedApiResponse<Starship> apiResponse = wrappedResponse.body();
        assertTrue(apiResponse.getCount() > 0);

        Starship starship = apiResponse.getResults().get(0);
        assertNotNull(starship);
    }

    private static class LoggingInterceptor implements Interceptor {
        @Override public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            Timber.d("Sending request %s on %s%n%s%n%n%s",
                    request.url(), chain.connection(), request.headers(), request.body());

            long t1 = System.currentTimeMillis();
            com.squareup.okhttp.Response response = chain.proceed(request);
            long t2 = System.currentTimeMillis();
            Timber.d("Received response for %s in %dms%n%s",
                    response.request().url(), (t2 - t1), response.headers());

            return response;
        }
    }

}