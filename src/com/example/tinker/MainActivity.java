package com.example.tinker;

import android.app.Activity;
import android.os.Bundle;
import com.example.tinker.views.Controller;
import com.example.tinker.views.Views;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @title Tinker
 * @version 1.0.0
 * @author Robert Marius Avram
 * @year 2014
 */
public class MainActivity extends Activity {

    /**
     *
     *
     * Main Activity is composed of a thread pool and a view controller.
     * Both which are accessible to each part of the application.
     *
     * It is good practice to use the threads and views supplied by the pool and controller, instead of creating
     * new ones. This reduces the overall overhead of the application and thus accelerates its procedures.
     */
    public static ExecutorService threadPool;
    public static Controller viewController;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        threadPool = Executors.newCachedThreadPool();
        viewController = new Controller(this);
        viewController.setDisplayView(Views.MAIN);
    }

}
