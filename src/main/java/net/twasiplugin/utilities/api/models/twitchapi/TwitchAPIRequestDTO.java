package net.twasiplugin.utilities.api.models.twitchapi;

import net.twasi.core.database.models.User;
import net.twasi.twitchapi.kraken.channels.response.ChannelDTO;

import static net.twasi.twitchapi.TwitchAPI.kraken;

public class TwitchAPIRequestDTO {

    private final ChannelDTO channelDTO;

    public TwitchAPIRequestDTO(User user) {
        channelDTO = kraken().channels().withAuth(user.getTwitchAccount().toAuthContext()).updateChannel(null, null);
    }

    public String game() {
        return channelDTO.getGame();
    }

    public String title() {
        return channelDTO.getStatus();
    }

    public int followers(){
        return channelDTO.getFollowers();
    }
}
