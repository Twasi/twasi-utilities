package de.merlinw.twasi.utilities.commands.title;

import de.merlinw.twasi.utilities.commands.BaseCommand;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import org.jetbrains.annotations.NotNull;

import static net.twasi.twitchapi.TwitchAPI.kraken;

public class Title extends BaseCommand {

    private String title;

    public Title(@NotNull TwasiCommandEvent e, @NotNull TwasiUserPlugin plugin) {
        super(e, plugin);
        this.title = commandArgs;
    }

    @Override
    protected String getCommandOutput() {
        if (!this.streamer.getUser().hasPermission(this.executor, "twasi.utilities.mod.title")) return null;

        if (this.title == null) return plugin.getTranslation("twasi.utilities.title.helptext");

        boolean success = kraken().channels().withAuth(this.streamer.getUser().getTwitchAccount().toAuthContext()).updateChannel(this.title, null) != null;
        if (success) return plugin.getTranslation("twasi.utilities.title.changed", this.title);
        else return plugin.getTranslation("twasi.utilities.title.requestfailed");
    }
}
