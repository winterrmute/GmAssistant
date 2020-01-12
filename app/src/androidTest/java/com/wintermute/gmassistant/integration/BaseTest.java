package com.wintermute.gmassistant.integration;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.NoMatchingViewException;

abstract class BaseTest
{
    static final String TEST_PLAYLIST = "example playlist";

    void clickOn(int... ids) throws InterruptedException
    {
        for (int id : ids)
        {
            try
            {
                onView(withId(id)).perform(click());
            } catch (NoMatchingViewException e)
            {
                Thread.sleep(100);
                onView(withId(id)).perform(click());
            }
        }
    }

    void clickOn(String... keys) throws InterruptedException
    {
        for (String key : keys)
        {
            try
            {
                onView(withText(key)).perform(click());
            } catch (NoMatchingViewException e)
            {
                Thread.sleep(100);
                onView(withText(key)).perform(click());
            }
        }
    }
}
