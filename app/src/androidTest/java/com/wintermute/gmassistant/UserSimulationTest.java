package com.wintermute.gmassistant;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

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

    @Rule
    public ActivityScenarioRule<GmAssistant> activityScenarioRule = new ActivityScenarioRule<>(GmAssistant.class);

    @Test
    public void test()
    {
        clickOn(R.id.manage_playlists, R.id.add_playlist);
        onView(withId(R.id.playlist_name)).perform(typeText("Hallo Verena"));
    }

    void clickOn(int... ids)
    {
        for (int id : ids)
        {
            onView(withId(id)).perform(click());
        }
    }
}
