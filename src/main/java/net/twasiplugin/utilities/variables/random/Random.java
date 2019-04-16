package net.twasiplugin.utilities.variables.random;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.models.Message.TwasiMessage;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.TwasiVariable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.twasi.twitchapi.TwitchAPI.tmi;

public class Random extends TwasiVariable {

    public Random(TwasiUserPlugin owner) {
        super(owner);
    }

    @Override
    public List<String> getNames() {
        return Collections.singletonList("random");
    }

    @Override
    public String process(String name, TwasiInterface inf, String[] params, TwasiMessage message) {
        List<String> all = new ArrayList<>(tmi().chatters().getByName("#" + inf.getStreamer().getUser().getTwitchAccount().getUserName()).getChatters().getAll());
        Collections.shuffle(all);
        try {
            return all.get(0);
        } catch (Exception e) {
            return "NO_CHATTERS";
        }
    }
}
