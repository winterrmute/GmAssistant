package com.wintermute.soundboard.services.database;

import static junit.framework.TestCase.assertEquals;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.platform.app.InstrumentationRegistry;
import com.wintermute.soundboard.model.Playlist;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.UUID;

/**
 * Test db functionality.
 */
@RunWith(JUnit4.class)
public class DbManagerAndroidTest
{

    SQLiteDatabase db;
    DbManager dbManager;
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
        db = new DbManager(ctx).getWritableDatabase();
        dbManager = new DbManager(ctx);

        long playlistId = UUID.randomUUID().getMostSignificantBits();
        long relatedPlaylistId = UUID.randomUUID().getMostSignificantBits();

        Playlist playlist = new Playlist();
        playlist.setName("userPlaylist");
        playlist.setId(playlistId);
        playlist.setPlaylistId(relatedPlaylistId);

        String idColumn = "id";
        String nameColumn = "name";
        String playlistIdColumn = "playlist_id";

        ContentValues values = new ContentValues();
        values.put(idColumn, playlist.getId());
        values.put(nameColumn, playlist.getName());
        values.put(playlistIdColumn, playlist.getPlaylistId());

        String tableName = "user_playlist";
        db.insert(tableName, null, values);

        Cursor cursor = db.rawQuery("SELECT name FROM " + tableName + " WHERE id ='" + playlistId + "'", null);
        String name = "";
        if (cursor != null)
        {
            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndexOrThrow(nameColumn));
        }
        assertEquals("userPlaylist", name);
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
