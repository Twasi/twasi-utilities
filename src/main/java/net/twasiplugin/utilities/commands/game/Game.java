package net.twasiplugin.utilities.commands.game;

import net.twasiplugin.utilities.commands.BaseCommand;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import net.twasi.twitchapi.kraken.channels.response.ChannelDTO;
import org.jetbrains.annotations.NotNull;

import static net.twasi.twitchapi.TwitchAPI.kraken;

public class Game extends BaseCommand {

    private String game;

    public Game(@NotNull TwasiCommandEvent e, @NotNull TwasiUserPlugin plugin) {
        super(e, plugin);
        this.game = commandArgs;
    }

    @Override
    protected String getCommandOutput() {
        if (!this.streamer.getUser().hasPermission(this.executor, "twasi.utilities.mod.game") || this.game == null) {
            ChannelDTO channelDTO = kraken().channels().withAuth(streamer.getUser().getTwitchAccount().toAuthContext()).updateChannel(null, null);
            if (channelDTO != null) return plugin.getTranslation("twasi.utilities.game.info", channelDTO.getGame());
            return plugin.getTranslation("twasi.utilities.game.info.failed");
        }

        boolean success = kraken().channels().withAuth(this.streamer.getUser().getTwitchAccount().toAuthContext()).updateChannel(null, this.game) != null;
        if (success) return plugin.getTranslation("twasi.utilities.game.change", this.game);
        else return plugin.getTranslation("twasi.utilities.game.change.failed");
    }
}
