package net.twasiplugin.utilities.commands.title;

import net.twasi.core.models.Streamer;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.customcommands.TwasiCustomCommandEvent;
import net.twasi.core.plugin.api.customcommands.TwasiPluginCommand;
import net.twasi.twitchapi.kraken.channels.response.ChannelDTO;

import static net.twasi.twitchapi.TwitchAPI.kraken;

public class Title extends TwasiPluginCommand {

    public Title(TwasiUserPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getCommandName() {
        return "title";
    }

    @Override
    public void process(TwasiCustomCommandEvent event) {
        Streamer streamer = event.getStreamer();
        if (!streamer.getUser().hasPermission(event.getSender(), "twasi.utilities.mod.title") || !event.hasArgs()) {
            ChannelDTO channelDTO = kraken().channels().withAuth(streamer.getUser().getTwitchAccount().toAuthContext()).updateChannel(null, null);
            if (channelDTO != null) event.reply(getTranslation("twasi.utilities.title.info", channelDTO.getStatus()));
            else event.reply(getTranslation("twasi.utilities.title.info.failed"));
            return;
        }
        String title = event.getArgsAsOne();
        boolean success = kraken().channels().withAuth(streamer.getUser().getTwitchAccount().toAuthContext()).updateChannel(title, null) != null;
        if (success) event.reply(getTranslation("twasi.utilities.title.change", title));
        else event.reply(getTranslation("twasi.utilities.title.change.failed"));
    }
}
