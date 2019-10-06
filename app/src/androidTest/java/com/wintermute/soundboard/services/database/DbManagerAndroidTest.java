package com.wintermute.soundboard.services.database;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.platform.app.InstrumentationRegistry;
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
        db = new DbManager(ctx).getWritableDatabase();
        dbManager = new DbManager(ctx);
        assertTrue(db.isOpen());
    }

    /**
     * Creates an object, updates it and removes it at last.
     */
    @Test
    public void writeAndRead()
    {

    }

    @Test
    public void createTable(){
        dbManager.createTable("tableName");
        dbManager.dropTable("tableName");
    }

    /**
     * Cleans up the test environment.
     */
    @After
    public void tearDown()
    {

        dbManager.close();
    }
}
