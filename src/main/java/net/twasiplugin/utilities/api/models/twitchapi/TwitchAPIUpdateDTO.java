package net.twasiplugin.utilities.api.models.twitchapi;

import net.twasi.core.database.models.User;
import net.twasi.twitchapi.kraken.channels.response.ChannelDTO;

import static net.twasi.twitchapi.TwitchAPI.kraken;

public class TwitchAPIUpdateDTO {

    private User user;

    public TwitchAPIUpdateDTO(User user) {
        this.user = user;
    }

    public boolean game(String game) {
        return update(null, game);
    }

    public boolean title(String title) {
        return update(title, null);
    }

    public boolean channel(String title, String game) {
        return update(title, game);
    }

    private boolean update(String title, String game) {
        title = title.trim();
        game = game.trim();
        ChannelDTO channelDTO = kraken().channels().withAuth(user.getTwitchAccount().toAuthContext()).updateChannel(title, game);
        return channelDTO.getStatus().equalsIgnoreCase(title) && channelDTO.getGame().equalsIgnoreCase(game);
    }
}
