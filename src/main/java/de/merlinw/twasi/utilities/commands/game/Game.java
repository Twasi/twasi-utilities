package de.merlinw.twasi.utilities.commands.game;

import de.merlinw.twasi.utilities.Plugin;
import de.merlinw.twasi.utilities.commands.BaseCommand;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import org.jetbrains.annotations.NotNull;

import static net.twasi.twitchapi.TwitchAPI.kraken;

public class Game extends BaseCommand {
    private String game;

    public Game(@NotNull TwasiCommandEvent e, @NotNull TwasiUserPlugin plugin) {
        super(e, plugin);
        this.game = Plugin.getCommandArgs(this.command);
    }

    @Override
    protected String getCommandOutput() {
        if (!this.streamer.getUser().hasPermission(this.executor, "twasi.utilities.mod.game"))
            return null;

        if (this.game == null)
            return plugin.getTranslation("twasi.utilities.game.helptext");

        kraken().channels().withAuth(this.streamer.getUser().getTwitchAccount().toAuthContext()).updateChannel(null, this.game);
        return plugin.getTranslation("twasi.utilities.game.changed", this.game);
    }
}
