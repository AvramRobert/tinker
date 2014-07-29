package com.example.tinker.views.interfaces;


import com.example.tinker.views.Views;

/**
 * Created by root on 7/19/14.
 */
public interface DisplayViewController {

    public void setDisplayView(Views view);

    public DisplayView getCurrentDisplayView();

    public DisplayView getView(Views title);

    public void initialize();


}
