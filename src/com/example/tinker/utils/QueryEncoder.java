package com.example.tinker.utils;

/**
 * Created by root on 6/30/14.
 */
public class QueryEncoder {

    /**
     * Based on a particular String, this method builds the imdb search URL needed.
     * The template for a search query is: http://imdb.find?q= <param1> + <param2> + <paramN>$s=all"
     * <p>
     * The search query String is taken and then divided based on the spaces between each word.
     * The space defines the input of a new parameter, therefor being linked to the others with a "+"
     *
     * @param query search String
     * @return complete URL with query
     */
    public static String buildSearchQuery(String query) {
        String[] params = query.split(" ");
        String domain = "http://imdb.com/find?q=";
        String end = "&s=all";
        StringBuilder q = new StringBuilder(domain);
        for (int i = 0; i < params.length; i++) {
            q.append(params[i]);
            if (i + 1 == params.length) q.append(end);
            else q.append("+");
        }
        return q.toString();
    }

    /**
     * Builds the URL for a database entry request.
     * The general template of such requests is: "http://imdb.com/<topic>/<id>"
     *
     * `However` - in the case of a movie entry, it is best to analyse the entirety of the cast list.
     *  This implies using an additional query to the existent domain, ergo: <domain> /fullcredits?ref_=tt_ov_st_sm
     *
     *  The extraction of the proper domain is rather somewhat troublesome, as the hrefs contained in the anchors generally
     *  have a `?ref...` String after the <id> parameter. This must then be excluded in this case, hence the use of "split()"
     * @param id String containing topic and id
     * @return Database entry URL
     */

    public static String buildDBRequest(String id) {
        String domain = "http://imdb.com";
        if (id.contains("title")) {
            return domain + id.split("\\?")[0] + "fullcredits?ref_=tt_ov_st_sm";
        }
        return domain + id;
    }


    public static String buildHref(String id) {
        return "http://imdb.com" + id;
    }


}
