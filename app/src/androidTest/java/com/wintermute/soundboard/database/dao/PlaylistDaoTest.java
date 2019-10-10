package com.wintermute.soundboard.database.dao;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import com.wintermute.soundboard.model.Playlist;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test PlaylistDao.
 */
@RunWith(JUnit4.class)
public class PlaylistDaoTest
{

    Context ctx;

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
        PlaylistDao dao = new PlaylistDao(ctx);

        Playlist playlist = new Playlist();
        playlist.setName("customPlaylist");

        playlist.setId(dao.insert(playlist));
        assertTrue(dao.getAll().size() > 0);

        playlist.setName("Lolz");
        dao.update(playlist);
        assertEquals("Lolz", dao.getPlaylist(playlist.getId()).getName());

        dao.delete(playlist);
        assertEquals(0, dao.getPlaylistNames().size());
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
