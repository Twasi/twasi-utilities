package net.twasiplugin.utilities.api.models;

import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import net.twasi.twitchapi.kraken.channels.response.ChannelDTO;
import net.twasi.twitchapi.options.TwitchRequestOptions;

import static net.twasi.twitchapi.TwitchAPI.helix;
import static net.twasi.twitchapi.TwitchAPI.kraken;

public class UtilitiesDTO {

    private User user;

    public UtilitiesDTO(User user) {
        this.user = user;
    }

    public boolean updateTitle(String title) {
        return updateChannel(title, null);
    }

    public boolean updateGame(String game) {
        return updateChannel(null, game);
    }

    public boolean updateChannel(String title, String game) {
        ChannelDTO channelDTO = kraken().channels().withAuth(user.getTwitchAccount().toAuthContext()).updateChannel(title, game);
        boolean t = title == null || channelDTO.getStatus().equalsIgnoreCase(title);
        boolean g = game == null || channelDTO.getGame().equalsIgnoreCase(game);
        return t && g;
    }

    public String getTitle() {
        TwitchAccount user = this.user.getTwitchAccount();
        try {
            return kraken().channels().withAuth(user.toAuthContext()).updateChannel(null, null).getStatus();
        } catch (Throwable t) {
            return null;
        }
    }

    public String getGame() {
        TwitchAccount user = this.user.getTwitchAccount();
        try {
            return kraken().channels().withAuth(user.toAuthContext()).updateChannel(null, null).getGame();
        } catch (Throwable t) {
            return null;
        }
    }

}
