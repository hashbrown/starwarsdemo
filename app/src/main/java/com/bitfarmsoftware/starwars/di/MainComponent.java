package com.bitfarmsoftware.starwars.di;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;

/**
 * @author Brad Armstrong, Bitfarm Software
 */
@Singleton
@Component(modules = {AndroidModule.class,ApiModule.class})
public interface MainComponent extends StarWarsComponent {
}
