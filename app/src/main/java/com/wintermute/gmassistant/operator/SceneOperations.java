package com.wintermute.gmassistant.operator;

import android.content.Context;
import com.wintermute.gmassistant.database.dao.SceneDao;
import com.wintermute.gmassistant.handlers.PlayerHandler;
import com.wintermute.gmassistant.model.Scene;

import java.util.ArrayList;

/**
 * This class is responsible for informing views that data has changed, doing basic operations on models and receiving
 * events from views.
 *
 * @author wintermute
 */
public class SceneOperations
{
    private Context sceneAppContext;
    private SceneDao dao;

    public SceneOperations(Context ctx){
        this.sceneAppContext = ctx;
        dao = new SceneDao(sceneAppContext);
    }

    public ArrayList<Scene> loadViewElements()
    {
        //get Elements that should be displayed.
        return dao.getAll();
    }

    public void deleteElement(Scene scene){
        dao.delete(scene);
    }

    public void notifyChangedView()
    {
        // send changes to the View
    }

    public void updateModel()
    {
        // send changes to the Model
    }

    public void startScene(String id){
        PlayerHandler handler = new PlayerHandler(sceneAppContext);
        handler.startPlayerByScene(id);
    }
}
