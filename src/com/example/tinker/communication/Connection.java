package com.example.tinker.communication;

import com.example.tinker.MainActivity;
import com.example.tinker.views.MainView;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;

/**
 * Created by root on 6/29/14.
 */
public class Connection {

    /**
     * Method that gets a particular website and returns the HTML-Document of it.
     * Threaded operation.
     * @param u website url
     * @return website HTML-Document
     */
    public static Document get(String u) throws ExecutionException, InterruptedException {
        Callable<Document> c = new Callable<Document>() {
            @Override
            public Document call() throws Exception {
                URL url;
                HttpURLConnection connection = null;
                InputStream stream = null;
                Document html;
                try{
                    url = new URL(u);
                    connection = (HttpURLConnection) url.openConnection();
                    stream = connection.getInputStream();
                }catch(IOException e) {
                    e.printStackTrace();
                }
                try {
                    html = readStream(stream);
                } finally {
                    assert connection != null;
                    connection.disconnect();
                }
                return html;
            }
            };
              Future<Document> callable = MainActivity.threadPool.submit(c);
              if(callable.isDone()) System.out.print("Connection success! HTML-Document retrieved");
            return callable.get();
    }

    /**
     * Reads byte stream and converts it to String then returns an `Document`-object based on it.
     * @param stream byte stream
     * @return HTML-Document
     */
    public static Document readStream(InputStream stream) {
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(stream, writer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Jsoup.parse(writer.toString());
    }
}
