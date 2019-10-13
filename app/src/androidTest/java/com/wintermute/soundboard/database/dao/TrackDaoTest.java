package com.wintermute.soundboard.database.dao;


import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import com.wintermute.soundboard.database.dto.TrackDto;
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
    private static TrackDto track;
    private static TrackDao dao;

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
        assertEquals("/here/be/path", dao.getTrack(track.getId()).getPath());

        track.setName("changed");
        dao.update(track);
        assertEquals("changed", dao.getTrack(track.getId()).getName());

        dao.delete(track);
        assertEquals(0, dao.getAll().size());
    }

    @BeforeClass
    public static void setUp(){
        ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        dao = new TrackDao(ctx);
        track = new TrackDto();
        track.setName("sample");
        track.setPath("/here/be/path");
        track.setArtist("Dj-yo-mama");
        track.setSceneId("0");
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
