package com.example.tinker.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by root on 7/28/14.
 */
public class Singles {

    private Set<String> single;
    public Singles(Set<String> single) {
        this.single = single;
    }

    public Jobs match(Jobs jobs) {
        List<JobEntry> c = new ArrayList<>();
        for(JobEntry j: jobs.getEntries()) {
            for(String s: single) {
                if(j.getJob().toLowerCase().equals(s.toLowerCase()) || j.getTitle().toLowerCase().equals(s.toLowerCase()) || j.getLink().toLowerCase().equals(s.toLowerCase())) {
                    c.add(j);
                }
            }
        }
        return new Jobs(jobs.getName(), jobs.getType(), jobs.getLink(), c);
    }

    public Jobs matchWithEntries(Jobs jobs) {
        List<JobEntry> c = new ArrayList<>();
        for(String s: single) {
            for(JobEntry j: jobs.getEntries()) {
                if(s.toLowerCase().equals(j.getJob().toLowerCase()) || s.toLowerCase().equals(j.getTitle().toLowerCase()) || s.toLowerCase().equals(j.getLink().toLowerCase())) {
                    c.add(j);
                    break;
                }
            }
        }
        return new Jobs(jobs.getName(), jobs.getType(), jobs.getLink(), c);
    }

    public Set<String> getEntries() {
        return this.single;
    }
}
