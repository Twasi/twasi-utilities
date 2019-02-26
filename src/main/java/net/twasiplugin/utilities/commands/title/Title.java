package net.twasiplugin.utilities.commands.title;

import net.twasiplugin.utilities.commands.BaseCommand;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import net.twasi.twitchapi.kraken.channels.response.ChannelDTO;
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
        if (!this.streamer.getUser().hasPermission(this.executor, "twasi.utilities.mod.title") || this.title == null) {
            ChannelDTO channelDTO = kraken().channels().withAuth(streamer.getUser().getTwitchAccount().toAuthContext()).updateChannel(null, null);
            if (channelDTO != null) return plugin.getTranslation("twasi.utilities.title.info", channelDTO.getStatus());
            return plugin.getTranslation("twasi.utilities.title.info.failed");
        }

        boolean success = kraken().channels().withAuth(this.streamer.getUser().getTwitchAccount().toAuthContext()).updateChannel(this.title, null) != null;
        if (success) return plugin.getTranslation("twasi.utilities.title.change", this.title);
        else return plugin.getTranslation("twasi.utilities.title.change.failed");
    }
}
