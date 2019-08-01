package net.twasiplugin.utilities.commands.hosts;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.customcommands.TwasiCustomCommandEvent;
import net.twasi.core.plugin.api.customcommands.TwasiPluginCommand;
import net.twasiplugin.utilities.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Hosts extends TwasiPluginCommand {

    public Hosts(TwasiUserPlugin plugin) {
        super(plugin);
    }

    public String getCommandName() {
        return "hosts";
    }

    @Override
    public void process(TwasiCustomCommandEvent event) {
        try {
            TwitchAccount streamer = event.getStreamer().getUser().getTwitchAccount();
            if (streamer.getUserName().equalsIgnoreCase("spendendose")) return;
            JsonObject object = new JsonParser().parse(Plugin.getApiContent("https://tmi.twitch.tv/hosts?include_logins=1&target=" + streamer.getTwitchId())).getAsJsonObject();
            JsonArray streamers = object.get("hosts").getAsJsonArray();
            if (streamers.size() == 0) {
                event.reply(getTranslation("twasi.utilities.hosts.nohoster", streamer.getDisplayName()));
                return;
            }

            List<String> userNames = new ArrayList<>();
            for (JsonElement elem : streamers)
                userNames.add(elem.getAsJsonObject().get("host_display_name").getAsString());

            String streamerUserNames = String.join(", ", userNames);

            event.reply(getTranslation("twasi.utilities.hosts.success", streamer.getDisplayName(), streamerUserNames.substring(2), userNames.size()));
        } catch (Exception e) {
            event.reply(getTranslation("twasi.utilities.hosts.failed"));
        }
    }

    @Override
    public boolean allowsTimer() {
        return true;
    }
}
