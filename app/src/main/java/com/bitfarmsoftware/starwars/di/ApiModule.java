package com.bitfarmsoftware.starwars.di;

import android.content.Context;
import android.net.Uri;

import com.bitfarmsoftware.starwars.BuildConfig;
import com.bitfarmsoftware.starwars.model.UriTypeAdapter;
import com.bitfarmsoftware.starwars.service.remote.StarWarsApi;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import timber.log.Timber;

/**
 * @author Brad Armstrong, Bitfarm Software
 */
@Module
public class ApiModule {

    private static final long HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;

    @Singleton
    @Provides
    public OkHttpClient getOkClient(Context ctx){
        OkHttpClient client = new OkHttpClient();

        if (BuildConfig.DEBUG) {
            // add logging interceptor
            client.networkInterceptors().add(new LoggingInterceptor());
            client.networkInterceptors().add(new StethoInterceptor());
        }

        // add http caching
        File baseDir = ctx.getCacheDir();
        if (baseDir != null) {
            final File cacheDir = new File(baseDir, "HttpResponseCache");
            client.setCache(new Cache(cacheDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE));
        }
        return client;
    }

    @Singleton
    @Provides
    public StarWarsApi getApi(OkHttpClient client){

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriTypeAdapter())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://swapi.co/api/")
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(StarWarsApi.class);
    }

   private static class LoggingInterceptor implements Interceptor {
        @Override public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            Timber.d("Sending request %s on %s%n%s%n%n%s",
                    request.url(), chain.connection(), request.headers(), request.body());

            long t1 = System.currentTimeMillis();
            Response response = chain.proceed(request);
            long t2 = System.currentTimeMillis();
            Timber.d("Received response for %s in %dms%n%s",
                    response.request().url(), (t2 - t1), response.headers());

            return response;
        }
    }
}
