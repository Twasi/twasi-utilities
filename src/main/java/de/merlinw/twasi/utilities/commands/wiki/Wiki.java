package de.merlinw.twasi.utilities.commands.wiki;

import com.google.gson.JsonObject;
import de.merlinw.twasi.utilities.Plugin;
import de.merlinw.twasi.utilities.commands.BaseCommand;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;

public class Wiki extends BaseCommand {
    private String query;

    public Wiki(@NotNull TwasiCommandEvent e, @NotNull TwasiUserPlugin plugin) {
        super(e, plugin);
        this.query = Plugin.getCommandArgs(this.command);
    }

    @Override
    protected String getCommandOutput() {
        if(this.query == null) return plugin.getTranslation("twasi.utilities.wiki.helptext");
        String apiPrefix = plugin.getTranslation("twasi.utilities.wiki.apiprefix");
        String url = "https://" + apiPrefix + ".wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles=" + URLEncoder.encode(this.query.replaceAll(" ", "_"));
        JsonObject object = new JsonObject();
        try {
            object = parser.parse(Plugin.getApiContent(url)).getAsJsonObject();
            JsonObject pages = object.get("query").getAsJsonObject().get("pages").getAsJsonObject();
            JsonObject page = pages.get((String) (pages.keySet()).toArray()[0]).getAsJsonObject();
            String article = page.get("extract").getAsString();
            if(article.length() > 330) article = article.substring(0,330) + "...";
            String link = page.get("title").getAsString().replace(" ", "_");
            link = "https://" + apiPrefix + ".wikipedia.org/wiki/" + link;
            return plugin.getTranslation("twasi.utilities.wiki.result", article, link);
        } catch (NullPointerException e){
            e.printStackTrace();
            try{
                String suggestion = object.get("query").getAsJsonObject().get("normalized").getAsJsonArray().get(0).getAsJsonObject().get("from").getAsString();
                return plugin.getTranslation("twasi.utilities.wiki.correct", this.query, suggestion.replaceAll("_", " "));
            } catch(NullPointerException e1) {
                return plugin.getTranslation("twasi.utilities.wiki.noresult", this.query);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return plugin.getTranslation("twasi.utilities.wiki.requestfailed", this.query);
        }
    }

    private class WikipediaArticle{
        private String text, url;

        private WikipediaArticle(String text, String url){
            this.text = text;
            this.url = url;
        }

        public String getText() {
            return text;
        }

        public String getUrl() {
            return url;
        }
    }
}
