package net.twasiplugin.utilities.commands.wiki;

import net.twasiplugin.utilities.commands.BaseCommand;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import org.jetbrains.annotations.NotNull;

public class Wiki extends BaseCommand {

    private String query;

    public Wiki(@NotNull TwasiCommandEvent e, @NotNull TwasiUserPlugin plugin) {
        super(e, plugin);
        this.query = commandArgs;
    }

    @Override
    protected String getCommandOutput() {

        if (this.query == null || this.query.equals("") || this.query.equals(" "))
            return this.plugin.getTranslation("twasi.utilities.wiki.helptext");

        WikiArticle article = new WikiArticle(this.query, this.plugin.getTranslation("twasi.utilities.wiki.apiprefix"));

        int shorten = 500;
        if (article.getProperlyFormattedContent() != null)
            shorten = article.getProperlyFormattedContent().length() - 1;
        String message;
        switch (article.getState()) {
            case NO_RESULT:
                return plugin.getTranslation("twasi.utilities.wiki.noresult", this.query);
            case SUGGESTION:
                String prefix = plugin.getTranslation("twasi.utilities.wiki.suggestion", this.query, article.getContentQuery()) + " ";
                String suffix = plugin.getTranslation("twasi.utilities.wiki.result", article.getContent(), article.getURL());
                message = prefix + suffix;
                while (message.length() > 400)
                    message = prefix + plugin.getTranslation("twasi.utilities.wiki.result", article.getContent().substring(0, --shorten), article.getURL());
                return message;
            case QUERY_ERROR:
            default:
                return plugin.getTranslation("twasi.utilities.wiki.requestfailed", this.query);
            case OK:
                message = plugin.getTranslation("twasi.utilities.wiki.result", article.getContent(), article.getURL());
                while (message.length() > 400)
                    message = plugin.getTranslation("twasi.utilities.wiki.result", article.getContent().substring(0, --shorten), article.getURL());
                return message;
        }
    }
}
