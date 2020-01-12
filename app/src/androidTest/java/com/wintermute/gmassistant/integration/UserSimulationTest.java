package com.wintermute.gmassistant.integration;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import com.wintermute.gmassistant.GmAssistant;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test FileBrowser.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserSimulationTest extends BaseTest
{

    @Rule
    public ActivityScenarioRule<GmAssistant> activityScenarioRule = new ActivityScenarioRule<>(GmAssistant.class);

    @Test
    public void managePlaylist()
    {

    }
}
