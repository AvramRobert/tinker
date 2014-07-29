package com.example.tinker.content;

import java.util.*;

/**
 * Created by root on 7/5/14.
 */
public class Jobs {
    private String name;
    private String type;
    private String link;
    private List<JobEntry> cv;

    public Jobs(String name, String type, String link, List<JobEntry> jobs) {
        this.name = name;
        this.type = type;
        this.link = link;
        this.cv = jobs;
    }

    public List<JobEntry> getEntries() {
        return cv;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getType() {
        return type;
    }

    public boolean isEmpty() {
        return cv.isEmpty();
    }

    /**
     * Takes the entries conveyed by the `cv` of this Object and only extracts those entries which match a particular list
     * of titles.
     *
     * This version or the analogous one provided by the `Singles`-class is used in the reconstruction of
     * the Jobs Object after the common list of titles (String) has been computed.
     *
     * The returned list is relative to the person to which the Jobs Object belongs.
     * @param partner List of common titles between n Jobs
     * @return new Jobs object containing only those entries that match the titles in `partner`.
     */
    public Jobs match(Jobs partner) {
        return partner.extract("title").match(this);
    }

    /**
     * Builds a consistent Jobs object containing only the unique `titles` found within the complete Object.
     *
     * It is important to note that the Jobs object returned by this method does not in any way contain
     * a complete list of all important information. It merely aids the method used to populate the view
     * with necessary links and provides the mapped information with the necessary keys.
     *
     * If the complete list were to be used, the view would ultimately contain duplicate entries, as a particular
     * person or film can have multiple contributions to the same
     * @return Jobs object with consistent `titles` and `links`
     */
    public Jobs removeDuplicates() {
        return this.extract("title").matchWithEntries(this);
    }

    /**
     * Extracts the selected attribute from the whole Jobs Object and returns a Singles Object
     * containing only that list of elements.
     *
     * Extraction automatically removes redundancy.
     * @param attribute String attribute
     * @return Singles Object containing list
     */
    public Singles extract(String attribute) {
        Set<String> single = new LinkedHashSet<String>();
        ListIterator<JobEntry> it = cv.listIterator();
        while(it.hasNext()) {
            switch (attribute) {
                case "job":
                    single.add(it.next().getJob());
                    break;
                case "link":
                    single.add(it.next().getLink());
                    break;
                default:
                    single.add(it.next().getTitle());
                    break;
            }
        }
        return new Singles(single);
    }


    /**
     * It takes a number of n lists contained in multiple "Jobs" wrapper objects and retrieves the
     * common entries between them.
     *
     * The result is a List of Jobs Objects that contain the entries of common jobs relative to each person
     * to which that Jobs Object belongs.
     * @param entries Jobs Array of Jobs wrapper objects
     * @return List of sets of common jobs for every searched person
     */
    public static List<Jobs> compareLists(Jobs... entries) {
        Set<String> tmp = new LinkedHashSet<>();
        List<Jobs> all = new ArrayList<>();
        for (int i = 0; i < entries.length; i++) {
            Set<String> elm = entries[i].extract("title").getEntries();
            if (i == 0) tmp = elm;
            else {
                tmp.retainAll(elm);
                if (tmp.size() == 0) {
                    tmp.clear();
                    break;
                }
            }
        }
        Singles common = new Singles(tmp);
        for (Jobs job : entries) {
            all.add(common.match(job));
        }
        return all;
    }


    public static List<JobEntry> matchLists(List<JobEntry> l1, List<JobEntry> l2, String criteria) {
        //TODO: ADD AS FUTURE ENHANCEMENT TO ALLOW A BROADER SELECTIVITY
        return null;
    }

    public Jobs filter(String... criteria) {
        List<JobEntry> js = new ArrayList<>();
        for(JobEntry j: cv) {
            for(String s: criteria) {
                if(j.getJob().toLowerCase().equals(s.toLowerCase()) || j.getTitle().toLowerCase().equals(s.toLowerCase())) {
                  js.add(j);
                }
            }
        }
        return new Jobs(name, type, link, js);
    }
}
