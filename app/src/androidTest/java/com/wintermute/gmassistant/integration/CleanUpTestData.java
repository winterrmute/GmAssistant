package com.wintermute.gmassistant.integration;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.wintermute.gmassistant.GmAssistant;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.integration.BaseTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CleanUpTestData extends BaseTest
{

    @Rule
    public ActivityScenarioRule<GmAssistant> activityScenarioRule = new ActivityScenarioRule<>(GmAssistant.class);

    @Test
    public void cleanUpData() throws InterruptedException
    {
        deleteTestPlaylist();
    }

    private void deleteTestPlaylist() throws InterruptedException
    {
        clickOn(R.id.manage_playlists);
        onView(withText(TEST_PLAYLIST)).perform(longClick());
        Thread.sleep(100);
        clickOn("delete");
    }
}
