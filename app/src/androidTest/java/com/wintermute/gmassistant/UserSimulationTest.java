package com.wintermute.gmassistant;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test FileBrowser.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserSimulationTest
{

    public static final String TEST_PLAYLIST = "example playlist";
    @Rule
    public ActivityScenarioRule<GmAssistant> activityScenarioRule = new ActivityScenarioRule<>(GmAssistant.class);

    @Test
    public void test()
    {
        clickOn(R.id.manage_playlists, R.id.add_playlist);
        onView(withId(R.id.playlist_name)).perform(typeText(TEST_PLAYLIST));
        clickOn(R.id.browse_device);
        clickOn("files");
        clickOn(R.id.select_current_directory, R.id.create_playlist_submit);
        ViewInteraction createdPlaylist = onView(withText(TEST_PLAYLIST));
        createdPlaylist.check(matches(isDisplayed()));
        createdPlaylist.perform(longClick());
        onView(withText("delete")).perform(longClick());
    }

    private void clickOn(int... ids)
    {
        for (int id : ids)
        {
            onView(withId(id)).perform(click());
        }
    }

    private void clickOn(String... keys)
    {
        for (String key : keys)
        {
            onView(withText(key)).perform(click());
        }
    }
}
