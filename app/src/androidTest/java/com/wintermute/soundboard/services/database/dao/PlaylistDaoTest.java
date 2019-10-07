package com.wintermute.soundboard.services.database.dao;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

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
        PlaylistDao playlistDao = new PlaylistDao(ctx);

        Playlist playlist = new Playlist();
        playlist.setId(10);
        playlist.setName("customPlaylist");
        playlist.setContentId(11);

        playlistDao.insert(playlist);

        assertNotNull(playlistDao.getByName("customPlaylist"));

        playlist.setName("Lolz");
        playlistDao.update(playlist);
        assertEquals("Lolz", playlistDao.getByName("Lolz").getName() );

        playlistDao.deleteByNameMatching(playlist);
        assertEquals(0, playlistDao.getPlaylistNames().size());
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
