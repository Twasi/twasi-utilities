package net.twasiplugin.utilities.api.models.twitchapi;

import net.twasi.core.database.models.User;

public class TwitchAPIDTO {

    private User user;

    public TwitchAPIDTO(User user) {
        this.user = user;
    }

    public TwitchAPIRequestDTO retrieve() {
        return new TwitchAPIRequestDTO(user);
    }

    public TwitchAPIUpdateDTO update() {
        return new TwitchAPIUpdateDTO(user);
    }

}
