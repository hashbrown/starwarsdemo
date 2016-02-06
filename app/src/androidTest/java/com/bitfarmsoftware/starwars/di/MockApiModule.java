package com.bitfarmsoftware.starwars.di;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.bitfarmsoftware.starwars.service.remote.MockStarWarsApi;
import com.bitfarmsoftware.starwars.service.remote.StarWarsApi;

import org.mockito.Mockito;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Brad Armstrong, Bitfarm Software
 */
@Module
public class MockApiModule {

    @Singleton
    @Provides
    public StarWarsApi getApi() { return new MockStarWarsApi(); }
}
