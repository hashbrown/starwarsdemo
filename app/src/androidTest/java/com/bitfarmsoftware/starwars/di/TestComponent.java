package com.bitfarmsoftware.starwars.di;

import com.bitfarmsoftware.starwars.StarshipDetailTest;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Brad Armstrong, Bitfarm Software
 */

@Singleton
@Component(modules = {AndroidModule.class,MockApiModule.class})
public interface TestComponent extends StarWarsComponent {
    void inject(StarshipDetailTest test);
}
