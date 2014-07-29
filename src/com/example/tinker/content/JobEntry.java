package com.example.tinker.content;

import com.example.tinker.utils.QueryEncoder;

/**
 * Created by root on 7/3/14.
 */

public class JobEntry {
    private String job, title, link;

    public JobEntry(String job, String title, String link) {
        this.job = job;
        this.title = title;
        this.link = link;
    }

    public String getJob() {return job;}

    public String getTitle() {return title;}

    public String getLink() { return link; }

    public String toAnchor() {
        return "<a href=\"" + QueryEncoder.buildHref(link) + "\">" + title + "</a>:";
    }
}
