package net.twasiplugin.utilities.variables.user;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.models.Message.TwasiMessage;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.TwasiVariable;

import java.util.Arrays;
import java.util.List;

public class User extends TwasiVariable {

    public User(TwasiUserPlugin owner) {
        super(owner);
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("user", "streamer");
    }

    @Override
    public String process(String name, TwasiInterface inf, String[] params, TwasiMessage message) {
        return message.getTwasiInterface().getStreamer().getUser().getTwitchAccount().getDisplayName();
    }
}
