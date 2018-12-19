package de.merlinw.twasi.utilities.commands.wiki;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.merlinw.twasi.utilities.Plugin;
import org.yaml.snakeyaml.util.UriEncoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static de.merlinw.twasi.utilities.commands.wiki.WikiArticle.QueryState.*;

public class WikiArticle {

    private String query;
    private String contentQuery;
    private String languageCode;

    private String title = null;
    private String content = null;

    private List<SearchResult> results;
    private List<String> suggestions;

    private QueryState state;

    private JsonParser parser;

    private boolean retry = false; // Set to true on first load retry to prevent retry loop

    public WikiArticle(String query) {
        this(query, "en");
    }

    public WikiArticle(String query, String languageCode) {
        this.query = query;
        this.languageCode = languageCode;
        this.parser = new JsonParser();

        loadWikiArticle();
    }

    private void loadWikiArticle() {
        this.results = new ArrayList<>();
        this.suggestions = new ArrayList<>();
        try {

            String searchUri = "https://" + this.languageCode + ".wikipedia.org/w/api.php" // Base API-URI
                    + "?action=query" + "&format=json" + "&list=search" + "&prop=extracts" + "&exintro=" + "&explaintext=" + "&utf8=1" // API settings
                    + "&srlimit=1" + "&srsearch=" + UriEncoder.encode(this.query); // Search parameters (query and limit)

            JsonObject queryResult = this.parser
                    .parse(Plugin.getApiContent(searchUri)).getAsJsonObject() // Get result and parse into JSON-Object
                    .get("query").getAsJsonObject(); // Extract the query result

            JsonObject searchInfo = queryResult.get("searchinfo").getAsJsonObject(); // Extract search info
            JsonArray searchResult = queryResult.get("search").getAsJsonArray(); // Extract search results

            for (int i = 0; i < searchResult.size(); i++) { // Loop through all search results
                JsonObject obj = searchResult.get(i).getAsJsonObject(); // Get current result
                this.results.add(new SearchResult( // Add a new search result to the list
                        obj.get("title").getAsString(), // Get title from result
                        obj.get("snippet").getAsString().replaceAll("<.*?>", "") // Get body and remove HTML
                ));
            }

            if (searchInfo.has("suggestion")) { // If the API has offered a suggestion
                String sgg = searchInfo.get("suggestion").getAsString(); // Extract suggestion
                this.suggestions.add(sgg); // Save to suggestions list
                if (this.results.size() > 0) { // If any result was found
                    String titleOfFoundResult = this.results.get(0).title; // Extract title of found result
                    if (titleOfFoundResult.equalsIgnoreCase(this.query)) { // If the results title matches the query
                        this.state = OK; // Set query state to successful
                        this.contentQuery = titleOfFoundResult; // Set content query string to title of found result
                    } else { // If the result doesn't match the query
                        if (!this.retry) {
                            this.retry = true;
                            this.query = sgg;
                            loadWikiArticle();
                            return;
                        }
                        this.state = SUGGESTION; // Set query state to suggestion
                        this.suggestions.add(titleOfFoundResult); // Add the not matching title to suggestions list
                        this.contentQuery = titleOfFoundResult; // Set content query string to title of found result
                    }
                } else { // If no result was found
                    this.state = SUGGESTION; // Set query state to suggestion
                    this.contentQuery = sgg; // Set content query string to the APIs suggestion
                }
            } else { // If the API has not offered a suggestion
                if (this.results.size() > 0) { // If any result was found
                    this.state = OK; // Set query state to successful
                    this.contentQuery = this.results.get(0).title; // Set content query string to title of found result
                } else { // If no result was found
                    this.state = NO_RESULT; // Set query state to unsuccessful
                    return; // End the query due to an unsuccessful search
                }
            }

            String contentQueryURI = "https://" + this.languageCode + ".wikipedia.org/w/api.php" // Base API-URI
                    + "?format=json" + "&action=query" + "&prop=extracts" + "&exintro=" + "&explaintext=" // API settings
                    + "&titles=" + UriEncoder.encode(this.contentQuery); // Search parameters (query only)

            JsonObject contentQueryResultPages = this.parser.parse(Plugin.getApiContent(contentQueryURI)).getAsJsonObject()
                    .get("query").getAsJsonObject()
                    .get("pages").getAsJsonObject();

            JsonObject contentQueryResult = contentQueryResultPages.get((String) (contentQueryResultPages.keySet()).toArray()[0]).getAsJsonObject();
            this.content = contentQueryResult.get("extract").getAsString();
            this.title = contentQueryResult.get("title").getAsString();

            if (this.content == null || this.content.equals("") || this.content.equals(" ")) this.state = NO_RESULT;
            if (this.retry) this.state = SUGGESTION;

        } catch (IOException e) {
            this.state = QUERY_ERROR;
        } catch (Exception e) {
            this.state = NO_RESULT;
        }
    }

    public String getQuery() {
        return query;
    }

    public String getContentQuery() {
        return contentQuery;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getURL() {
        return "https://" + this.languageCode + ".wikipedia.org/wiki/" + UriEncoder.encode(this.title);
    }

    public List<SearchResult> getResults() {
        return results;
    }

    public List<String> getSuggestion() {
        return suggestions;
    }

    public QueryState getState() {
        return state;
    }

    class SearchResult {
        private String title;
        private String content;

        private SearchResult(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }

    enum QueryState {
        NO_RESULT,
        SUGGESTION,
        QUERY_ERROR,
        OK
    }

}
