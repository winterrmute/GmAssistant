package com.wintermute.gmassistant.database.dao;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import com.wintermute.gmassistant.model.Track;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test PlaylistDao.
 */
@RunWith(JUnit4.class)
public class TrackDaoTest
{

    private static Context ctx;
    private static Track track;
    private static TrackDao dao;

    @BeforeClass
    public static void setUp()
    {
        ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        dao = new TrackDao(ctx);
        track = new Track();
        track.setName("sample");
        track.setPath("/here/be/path");
        track.setArtist("Dj-yo-mama");
    }

    /**
     * Prepare the test environment.
     */
    @Before
    public void createDb()
    {
        ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    /**
     * Creates an object, updates it and removes it at last.
     */
    @Test
    public void writeAndRead()
    {
        track.setId(dao.insert(track));
        assertTrue(dao.getAll().size() > 0);
        assertNotNull(track.getId());
        assertEquals("/here/be/path", dao.getTrack("path", track.getId()).getPath());

        track.setName("changed");
        dao.update(track);
        assertEquals("changed", dao.getTrack("name", track.getId()).getName());

        dao.deleteById(track.getId());
        assertEquals(0, dao.getAll().size());
    }

    /**
     * Cleans up the test environment.
     */
    @After
    public void tearDown()
    {
        ctx.deleteDatabase("soundboard");
    }
}
