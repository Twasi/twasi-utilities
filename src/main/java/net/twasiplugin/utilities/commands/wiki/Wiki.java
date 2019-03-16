package net.twasiplugin.utilities.commands.wiki;

import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.customcommands.TwasiCustomCommandEvent;
import net.twasi.core.plugin.api.customcommands.TwasiPluginCommand;

public class Wiki extends TwasiPluginCommand {

    public Wiki(TwasiUserPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getCommandName() {
        return "wiki";
    }

    @Override
    public void process(TwasiCustomCommandEvent event) {
        if (!event.hasArgs()) {
            event.reply(getTranslation("twasi.utilities.wiki.helptext"));
            return;
        }

        String query = event.getArgsAsOne();
        WikiArticle article = new WikiArticle(query, getTranslation("twasi.utilities.wiki.apiprefix"));

        int shorten = 500;
        if (article.getProperlyFormattedContent() != null)
            shorten = article.getProperlyFormattedContent().length() - 1;
        String message;
        switch (article.getState()) {
            case NO_RESULT:
                event.reply(getTranslation("twasi.utilities.wiki.noresult", query));
                return;
            case SUGGESTION:
                String prefix = getTranslation("twasi.utilities.wiki.suggestion", query, article.getContentQuery()) + " ";
                String suffix = getTranslation("twasi.utilities.wiki.result", article.getContent(), article.getURL());
                message = prefix + suffix;
                while (message.length() > 400)
                    message = prefix + getTranslation("twasi.utilities.wiki.result", article.getContent().substring(0, --shorten), article.getURL());
                event.reply(message);
                return;
            case QUERY_ERROR:
            default:
                event.reply(getTranslation("twasi.utilities.wiki.requestfailed", query));
                return;
            case OK:
                message = getTranslation("twasi.utilities.wiki.result", article.getContent(), article.getURL());
                while (message.length() > 400)
                    message = getTranslation("twasi.utilities.wiki.result", article.getContent().substring(0, --shorten), article.getURL());
                event.reply(message);
        }
    }
}
