package com.example.tinker;

import com.example.tinker.content.JobEntry;
import com.example.tinker.content.Jobs;
import com.example.tinker.tasks.ConnectionTasks;
import com.example.tinker.utils.Filter;
import com.example.tinker.utils.QueryEncoder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

public class Procedure {

    private String parameter;
    private String link;
    private String type;

    public void setSearchable(String s) {
        this.parameter = s;
    }

    /**
     * Retrieves the href of the first entry in the search result table.
     * Threaded operation as it involves complex selections throughout a fairly large document.
     *
     * @param html HTML Document
     * @return href of the first anchor
     */
    private String searchResultHref(Document html) throws ExecutionException, InterruptedException {
        Callable<String> href = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Element s = html.select("div.findSection").select("td.result_text").select("a").first();
                parameter = s.text();
                link = s.attr("href");
                return link;
            }
        };
        Future<String> callableFuture = MainActivity.threadPool.submit(href);
        if (callableFuture.isDone()) {
            System.out.println("Retrieved href!");
        }
        return callableFuture.get();
    }

    /**
     * Previous href retrieve Method.
     * <p>
     * Uses the `Filter`-class methods to retrieve data. (for more generalized use)
     * Because of performance reasons, this method has been considered deprecated, as the speed
     * of the inline `searchResultHref` delivers a much faster response than this sequenced method.
     *
     * @param html HTML Document
     * @return href of the first anchor
     */
    @Deprecated
    private String searchResultHrefOLD(Document html) {
        String divTopNav = "div.findSection";
        String tableSearch = "td.result_text";
        String[] quer = new String[]{divTopNav, tableSearch};
        Elements m = Filter.sequenceFilter(html, 2, quer);
        return Filter.extractAttribute(m, "a", "href");
    }


    /**
     * Extracts the single job titles a particular imdb person-entry has had so far.
     * These titles are conveniently summarized in the div#id = "jumpto" and
     * displayed as text in the inner anchors.
     *
     * @param html HTML Document
     * @return ArrayList of jobs titles
     */
    private ArrayList<String> jobs(Document html) throws ExecutionException, InterruptedException {
        Callable<ArrayList<String>> j = new Callable<ArrayList<String>>() {
            @Override
            public ArrayList<String> call() throws Exception {
                ArrayList<String> list = new ArrayList<>();
                Elements anchors = html.select("div#jumpto").select("a");
                for (Element e : anchors) {
                    list.add(e.text());
                }
                return list;
            }
        };
        Future<ArrayList<String>> jobs = MainActivity.threadPool.submit(j);
        return jobs.get();
    }


    /**
     * Extracts the relevant DB-entry information from the HTML.
     * <p>
     * It strips the filtered div tags (found very conveniently in the "filmo-category-section" class)
     * of their relevant information and merges them with the appropriate job titles creating a list
     * of JobEntries wrapped in a Jobs-object.
     * <p>
     * Threaded method, as the amount of complex document selection operations is too large.
     *
     * @param profile Document with the profile information
     * @return Jobs Object with the most important information
     */

    private Jobs personProfileContent(Document profile) throws ExecutionException, InterruptedException {
        Callable<Jobs> f = new Callable<Jobs>() {
            @Override
            public Jobs call() throws Exception {
                List<String> jobs = jobs(profile);
                List<JobEntry> j = new ArrayList<>();
                Elements first = profile.select("div.filmo-category-section");
                for (String all : jobs) {
                    Elements detail = first.select("div[id*=" + all.toLowerCase() + "]").select("b").select("a");
                    for (Element e : detail) {
                        j.add(new JobEntry(all, e.text(), e.attr("href")));
                    }
                }
                return new Jobs(parameter, type, link, j);
            }
        };
        Future<Jobs> callable = MainActivity.threadPool.submit(f);
        if (callable.isDone()) System.out.println("Profile content collected and processed!");
        return callable.get();
    }

    /**
     * Extracts the relevant DB-entry information from the HTML.
     * <p>
     * The selection process in a film comparison search is relatively more complex than that of a person comparison.
     * The difficulty lies in the fact, that there is no syntactical grouping of the `jobs` and `people` (as it were), meaning
     * that there is no html parent-child grouping-logic between what the person has done and the person itself.
     * The logic is rather sequentially grouped through pairs of <h4> and <table> tags.
     * The <h4> contains the so called `job`, whilst the following <table> contains a table of all the people who had that job, rather than
     * the <h4> containing the `job` and also the table.
     * <p>
     * The method therefor plucks these bits of information in pairs in order to successfully manage the mining operation.
     *
     * @param profile HTML Document
     * @return Jobs wrapper object with the given Jobs
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private Jobs filmProfileContent(Document profile) throws ExecutionException, InterruptedException {
        Callable<List<JobEntry>> movieEntries = new Callable<List<JobEntry>>() {
            @Override
            public List<JobEntry> call() throws Exception {
                Elements s = profile.select("h4.dataHeaderWithBorder, table.simpleTable.simpleCreditsTable, table.cast_list");
                Iterator<Element> it = s.iterator();
                List<JobEntry> jobs = new ArrayList<>();
                String job = null, name, link;
                boolean even = true;
                while (it.hasNext()) {
                    Element current = it.next();
                    if (even) {
                        job = current.text().split("\\(")[0];
                        even = false;
                    } else {
                        Elements anchors = current.select("tbody").select("tr");
                        if (current.attr("class").equals("cast_list")) {
                            Elements tmp = anchors.select("td.itemprop").select("a");
                            for (Element l : tmp) {
                                name = l.text();
                                link = l.attr("href");
                                jobs.add(new JobEntry(job, name, link));
                            }

                        } else {
                            Elements tmp = anchors.select("td.name").select("a");
                            for (Element l : tmp) {
                                name = l.text();
                                link = l.attr("href");
                                jobs.add(new JobEntry(job, name, link));
                            }
                        }
                        even = true;
                    }
                }
                return jobs;
            }
        };
        Future<List<JobEntry>> fetched = MainActivity.threadPool.submit(movieEntries);
        List<JobEntry> list = fetched.get();
        return new Jobs(parameter, type, link, list);

    }

    private Document getProfile(String url) throws ExecutionException, InterruptedException {
        setType(url);
        String request = QueryEncoder.buildDBRequest(url);
        return ConnectionTasks.retrieveHTML(request);
    }

    private String getSearchSuggestion() throws ExecutionException, InterruptedException {
        String imdbSearchURL = QueryEncoder.buildSearchQuery(parameter);
        return searchResultHref(ConnectionTasks.retrieveHTML(imdbSearchURL));
    }

    public void setType(String url) {
        type = (url.contains("name")) ? "person": "film";
    }

    /**
     * Fetches the relevant information of a search query.
     * Result contains three main entries all encapsulated in a Jobs-object:
     * 1. Job description (e.g: Actor, Producer, Director.. )
     * 2. Job title (e.g: The Wolf of Wall Street, Star Wars, The Dark Knight.. || Leonardo DiCaprio, Martin Scorsese.. )
     * 3. Link to entry(e.g: /title/<id>, /person/<id>.. )
     *
     * @return Jobs of the searched person
     * @throws java.util.concurrent.ExecutionException
     * @throws InterruptedException
     */
    public Jobs fetch() throws ExecutionException, InterruptedException {
        String searchSuggestionURL = getSearchSuggestion();
        Document profilePage = getProfile(searchSuggestionURL);
        return (type.equals("person")) ? personProfileContent(profilePage) : filmProfileContent(profilePage);
    }

}
