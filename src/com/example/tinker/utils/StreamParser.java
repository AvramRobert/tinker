package com.example.tinker.utils;

import android.text.Editable;

/**
 * Created by root on 7/5/14.
 */
public class StreamParser {


public static String[] parse(Editable stream) {
    StringBuilder b = new StringBuilder();
    String[] s = stream.toString().split(" ");
    s = clean(s);
    for(String ins: s) {
        b.append(ins).append(" ");
    }
    s = b.toString().split(",");
    return clean(s);

}

    public static String[] clean(String[] s) {
        for(int i =0; i<s.length; i++) {
            s[i] = s[i].trim();
        }
        return s;
    }
}
