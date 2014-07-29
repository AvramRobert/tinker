package com.example.tinker.views.interfaces;

import android.app.Activity;

/**
 * Created by root on 7/19/14.
 */
public interface DisplayView {

    public int getId();

    public void initialize(Activity activity);

    public void setError();

    public void populateDisplay(boolean reset);

    public void resetDisplay();

    public void setHyperlinks(String s);

    public void toggleDialog();

    public void dismissDialog();
}
