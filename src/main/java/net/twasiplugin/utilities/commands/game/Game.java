package net.twasiplugin.utilities.commands.game;

import net.twasi.core.models.Streamer;
import net.twasi.core.plugin.api.TwasiCustomCommand;
import net.twasi.core.plugin.api.TwasiCustomCommandEvent;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.twitchapi.kraken.channels.response.ChannelDTO;

import static net.twasi.twitchapi.TwitchAPI.kraken;

public class Game extends TwasiCustomCommand {

    public Game(TwasiUserPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getCommandName() {
        return "game";
    }

    @Override
    public void process(TwasiCustomCommandEvent event) {
        Streamer streamer = event.getStreamer();
        if (!streamer.getUser().hasPermission(event.getSender(), "twasi.utilities.mod.game") || !event.hasArgs()) {
            ChannelDTO channelDTO = kraken().channels().withAuth(streamer.getUser().getTwitchAccount().toAuthContext()).updateChannel(null, null);
            if (channelDTO != null) event.reply(getTranslation("twasi.utilities.game.info", channelDTO.getGame()));
            else event.reply(getTranslation("twasi.utilities.game.info.failed"));
            return;
        }
        String game = event.getArgsAsOne();
        boolean success = kraken().channels().withAuth(streamer.getUser().getTwitchAccount().toAuthContext()).updateChannel(null, game) != null;
        if (success) event.reply(getTranslation("twasi.utilities.game.change", game));
        else event.reply(getTranslation("twasi.utilities.game.change.failed"));
    }
}
