package com.bitfarmsoftware.starwars;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bitfarmsoftware.starwars.di.AndroidModule;
import com.bitfarmsoftware.starwars.di.DaggerTestComponent;
import com.bitfarmsoftware.starwars.di.TestComponent;
import com.bitfarmsoftware.starwars.model.PagedApiResponse;
import com.bitfarmsoftware.starwars.model.Starship;
import com.bitfarmsoftware.starwars.service.remote.MockStarWarsApi;
import com.bitfarmsoftware.starwars.service.remote.StarWarsApi;
import com.bitfarmsoftware.starwars.ui.StarshipDetailAdapter;
import com.bitfarmsoftware.starwars.ui.StarshipsActivity;
import com.bitfarmsoftware.starwars.util.rx.RxIdlingResource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import retrofit.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.plugins.RxJavaPlugins;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.allOf;

/**
 * @author Brad Armstrong, Bitfarm Software
 */
@RunWith(AndroidJUnit4.class)
public class StarshipDetailTest {


    @Rule
    public ActivityTestRule<StarshipsActivity> activityRule = new ActivityTestRule(StarshipsActivity.class) {
        @Override
        protected void beforeActivityLaunched() {
            setupDaggerComponent();
        }
    };

    @Inject
    StarWarsApi api;

    private Starship millFalcon;


    private void setupDaggerComponent() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        StarWarsApplication app = (StarWarsApplication) instrumentation.getTargetContext().getApplicationContext();
        TestComponent component = DaggerTestComponent.builder()
                .androidModule(new AndroidModule(app))
                .build();
        app.setApplicationComponent(component);

        component.inject(this);
    }

//    @BeforeClass
//    public static void registerIdler(){
//        RxJavaPlugins.getInstance().registerObservableExecutionHook(RxIdlingResource.get());
//    }


    @Before
    public void setup() {

//        Espresso.registerIdlingResources(RxIdlingResource.get());

        // retrieve the starship we want to test with and simulate its selection to show the detail fragment
        millFalcon = api.getStarships()
                .flatMap(new Func1<Response<PagedApiResponse<Starship>>, Observable<Starship>>() {
                    @Override
                    public Observable<Starship> call(final Response<PagedApiResponse<Starship>> response) {
                        return Observable.from(response.body().getResults());
                    }
                })
                .filter(new Func1<Starship, Boolean>() {
                    @Override
                    public Boolean call(final Starship starship) {
                        return starship.getName().equals("Millennium Falcon");
                    }
                })
                .toBlocking()
                .first();

        assertNotNull(millFalcon);


        // add delay to network
//        ((MockStarWarsApi)api).setNetworkDelay(0);

        activityRule.getActivity().onSelected(millFalcon);

    }

//    @After
//    public void teardown(){
//        Espresso.unregisterIdlingResources(RxIdlingResource.get());
//    }


    @Test
    public void shouldDisplayAllFilms() {
        onView(withId(R.id.starship_detail))
                .perform(RecyclerViewActions.scrollToHolder(matchingFilm(hasDescendant(withText("Episode 7: The Force Awakens")))))
                .perform(RecyclerViewActions.scrollToHolder(matchingFilm(hasDescendant(withText("Episode 6: Return of the Jedi")))))
                .perform(RecyclerViewActions.scrollToHolder(matchingFilm(hasDescendant(withText("Episode 5: The Empire Strikes Back")))))
                .perform(RecyclerViewActions.scrollToHolder(matchingFilm(hasDescendant(withText("Episode 4: A New Hope")))));

    }


    @Test
    public void shouldMaintainStateOnRotation(){
        // check that some details are initially displayed
        onView(withId(R.id.starship_detail))
                .perform(RecyclerViewActions.scrollToHolder(matchingShipDetails(allOf(
                        hasDescendant(withText("Model:")),
                        hasDescendant(withText("YT-1300 light freighter")),
                        hasDescendant(withText("Manufacturer:")),
                        hasDescendant(withText("Corellian Engineering Corporation"))
                ))));

        rotateScreen();

        // check for same state
        onView(withId(R.id.starship_detail))
                .perform(RecyclerViewActions.scrollToHolder(matchingShipDetails(allOf(
                        hasDescendant(withText("Model:")),
                        hasDescendant(withText("YT-1300 light freighter")),
                        hasDescendant(withText("Manufacturer:")),
                        hasDescendant(withText("Corellian Engineering Corporation"))
                ))));

    }

    private void rotateScreen() {
        Context context = InstrumentationRegistry.getTargetContext();
        int orientation
                = context.getResources().getConfiguration().orientation;

        Activity activity = activityRule.getActivity();
        activity.setRequestedOrientation(
                (orientation == Configuration.ORIENTATION_PORTRAIT) ?
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    private static ViewHolderMatcher matchingFilm(Matcher<View> matcher){ return new ViewHolderMatcher(StarshipDetailAdapter.FilmView.class, matcher);}

    private static ViewHolderMatcher matchingShipDetails(Matcher<View> matcher){ return new ViewHolderMatcher(StarshipDetailAdapter.StarshipDetailView.class, matcher);}


    private static class ViewHolderMatcher extends TypeSafeMatcher<RecyclerView.ViewHolder> {
        private Class clazz;
        private Matcher<View> itemMatcher = null;

        public ViewHolderMatcher() { }

        public ViewHolderMatcher(Class viewHolderClass, Matcher<View> itemMatcher) {
            this.clazz = viewHolderClass;
            this.itemMatcher = itemMatcher;
        }

        @Override
        public boolean matchesSafely(RecyclerView.ViewHolder viewHolder) {
            return this.clazz.isAssignableFrom(viewHolder.getClass()) && itemMatcher.matches(viewHolder.itemView);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("is assignable from " + clazz + " and matches " + itemMatcher);
        }
    }

}
