package com.wintermute.soundboard.services.database;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import com.wintermute.soundboard.services.database.entities.AudioFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test db functionality.
 */
@RunWith(JUnit4.class)
public class DbManagerAndroidTest
{
    private AudioFile.AudioFileDao audioFileDao;
    private DbManager db;

    @Before
    public void createDb()
    {
        Context ctx = InstrumentationRegistry.getInstrumentation().getContext();

        db = Room.inMemoryDatabaseBuilder(ctx, DbManager.class).build();
        audioFileDao = db.audioFileDao();
    }

    /**
     * Test CRUD
     */
    @Test
    public void writeAndRead()
    {
        AudioFile audioFile = new AudioFile.Builder("title")
            .withArtist("artist")
            .withDuration(5)
            .withPath("/this/is/path")
            .build();

        audioFileDao.insertAudioFile(audioFile);
        assertTrue(db.audioFileDao().getAll().size() > 0);
        assertEquals("title", db.audioFileDao().getAll().get(0).getTitle());

        audioFile.setArtist("yo-mama");
        audioFileDao.updateAudioFile(audioFile);
        assertEquals("yo-mama", audioFileDao.getAudioFile("title").getArtist());

        audioFileDao.deleteAudioFile(audioFile);
        assertEquals(0, audioFileDao.getAll().size());
    }

    @After
    public void tearDown(){
        db.clearAllTables();
        db.close();
    }
}
