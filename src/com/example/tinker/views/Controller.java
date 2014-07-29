package com.example.tinker.views;

import android.app.Activity;
import com.example.tinker.views.interfaces.DisplayView;
import com.example.tinker.views.interfaces.DisplayViewController;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by root on 7/8/14.
 */
public class Controller implements DisplayViewController {
        Activity activity;
        DisplayView current;
        Map<Views, DisplayView> viewMap = new HashMap<>();

    public Controller(Activity a) {
        this.activity = a;
        fillViewMap();
    }

    public void fillViewMap() {
        //ADD ALL VIEWS HERE
        viewMap.put(Views.MAIN, new MainView());
    }

    @Override
    public void setDisplayView(Views view) {
        current = viewMap.get(view);
        activity.setContentView(current.getId());
        initialize();
    }

    @Override
    public DisplayView getCurrentDisplayView() {
        return current;
    }

    @Override
    public DisplayView getView(Views view) {
        return viewMap.get(view);
    }

    @Override
    public void initialize() {
        current.initialize(activity);
    }

}
