package com.example.tinker.tasks;


import com.example.tinker.communication.Connection;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.concurrent.*;

/**
 * Created by root on 7/9/14.
 */
public class ConnectionTasks {

    /**
     * Task that retrieves the HTML Document of a particular website.
     *
     * @param url website URL
     * @return  HTML Document of the website
     */
    public static Document retrieveHTML(String url) {
        try {
            return Connection.get(url);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
