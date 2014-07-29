package com.example.tinker.utils;

import com.example.tinker.content.JobEntry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 7/3/14.
 */

public class Filter {

    /**
     * Applies selectors/queries in a sequence. It can either retain the selected tags or remove them all together.
     * The options fork the operation in:
     * 1. -> delete
     * 2. -> retain
     *
     * @param html HTML Document
     * @param option Remove or Retain
     * @param query Sequence of selectors
     * @return filtered Elements
     */
    //TODO: THREAD IT!!!!
    public static Elements sequenceFilter(Document html, int option, String... query) {
        Elements s = null;
        for (int i = 0; i < query.length; i++) {
            switch (option) {
                case 1:
                    if (i == 0) s = html.select(query[i]).remove();
                    else s = s.select(query[i]).remove();
                    break;
                case 2:
                    if (i == 0) s = html.select(query[i]);
                    else s = s.select(query[i]);
                    break;
            }
        }
        return s;
    }

    /**
     * Extracts the value of an attribute of the first tag of a document.
     * TODO: iterate through all the elements in the received document in order to extract the attribute value from any number of tags
     * @param html
     * @param tag
     * @param attribute
     * @return
     */
    public static String extractAttribute(Elements html, String tag, String attribute) {
        return html.select(tag).first().attr(attribute);
    }

    public List<String> toStringList(List<JobEntry> e) {
        List<String>  list = new ArrayList<String>();

        for(JobEntry title: e) {
            list.add(title.getTitle());
        }
        return list;
    }
    /**
     * Creates a `Document` from a given input.
     * @param input
     * @return
     */
    public Document toDocument(String input) {
        return Jsoup.parse(input);
    }
}

