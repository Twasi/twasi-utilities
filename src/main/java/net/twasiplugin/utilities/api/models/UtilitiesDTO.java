package net.twasiplugin.utilities.api.models;

import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import net.twasi.twitchapi.kraken.channels.response.ChannelDTO;
import net.twasi.twitchapi.options.TwitchRequestOptions;
import net.twasiplugin.utilities.api.models.twitchapi.TwitchAPIDTO;

import static net.twasi.twitchapi.TwitchAPI.helix;
import static net.twasi.twitchapi.TwitchAPI.kraken;

public class UtilitiesDTO {

    private User user;

    public UtilitiesDTO(User user) {
        this.user = user;
    }

    public TwitchAPIDTO getTwitchAPI() {
        return new TwitchAPIDTO(user);
    }

}
