package com.example.tinker.utils;

import android.content.Context;

import java.io.*;

/**
 * Created by root on 6/30/14.
 */
public class Exports {

        public static String defaultLog = "/log.txt";

        public static void exportToFile(Context context, String text) {
            try {
                FileOutputStream f_output = new FileOutputStream(new File(context.getFilesDir() + defaultLog));
                OutputStreamWriter o_writer = new OutputStreamWriter(f_output);
                Writer w = new BufferedWriter(o_writer);
                w.write(text);
                w.close();
            } catch (IOException e) {
                System.err.println("Could not write to or find specified file");
            }
        }
}
