package com.wintermute.gmassistant.integration;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import com.wintermute.gmassistant.GmAssistant;
import com.wintermute.gmassistant.R;
import org.junit.Rule;
import org.junit.Test;

public class PrepareTestEnvironment extends BaseTest
{
    @Rule
    public ActivityScenarioRule<GmAssistant> activityScenarioRule = new ActivityScenarioRule<>(GmAssistant.class);

    @Test
    public void setUpTestEnv() throws InterruptedException
    {
        //create playlist
        clickOn(R.id.manage_playlists, R.id.add_playlist);
        onView(withId(R.id.playlist_name)).perform(typeText(TEST_PLAYLIST));
        clickOn(R.id.browse_device);
        clickOn("files", "DeadLands", "gunslinger");
        clickOn(R.id.select_current_directory, R.id.create_playlist_submit);
        onView(withText(TEST_PLAYLIST)).check(matches(isDisplayed()));
    }
}
