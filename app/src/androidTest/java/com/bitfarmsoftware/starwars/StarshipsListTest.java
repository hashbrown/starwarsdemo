package com.bitfarmsoftware.starwars;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bitfarmsoftware.starwars.di.AndroidModule;
import com.bitfarmsoftware.starwars.di.DaggerTestComponent;
import com.bitfarmsoftware.starwars.di.TestComponent;
import com.bitfarmsoftware.starwars.ui.StarshipListViewAdapter;
import com.bitfarmsoftware.starwars.ui.StarshipsActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * @author Brad Armstrong, Bitfarm Software
 */
@RunWith(AndroidJUnit4.class)
public class StarshipsListTest {


    @Rule
    public ActivityTestRule<StarshipsActivity> activityRule = new ActivityTestRule(StarshipsActivity.class) {
        @Override
        protected void beforeActivityLaunched() {
            setupDaggerComponent();
        }
    };


    private void setupDaggerComponent() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        StarWarsApplication app = (StarWarsApplication) instrumentation.getTargetContext().getApplicationContext();
        TestComponent component = DaggerTestComponent.builder()
                .androidModule(new AndroidModule(app))
                .build();
        app.setApplicationComponent(component);
    }


    @Test
    public void shouldDisplayTheXWing() {
        onView(withId(R.id.starship_list))
                .perform(RecyclerViewActions.scrollToHolder(withStarshipNamed("X-wing")));
    }

    @Test
    public void shouldDisplayDeathStarDetailsScreenOnClick() {
        onView(withId(R.id.starship_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withText("Death Star Details"))
                .check(matches(isDisplayed()));
    }


    public static Matcher<RecyclerView.ViewHolder> withStarshipNamed(final String name) {

        return new BoundedMatcher<RecyclerView.ViewHolder, StarshipListViewAdapter.StarshipViewHolder>(StarshipListViewAdapter.StarshipViewHolder.class) {
            @Override
            public boolean matchesSafely(StarshipListViewAdapter.StarshipViewHolder holder) {
                boolean isMatches = false;

                if (holder.name != null) {
                    isMatches = ((name.equals(holder.name.getText().toString()))
                            && (holder.name.getVisibility() == View.VISIBLE));
                }
                return isMatches;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with starship named: " + name);
            }
        };
    }

}
