package net.twasiplugin.utilities.commands.hosts;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.plugin.api.TwasiCustomCommand;
import net.twasi.core.plugin.api.TwasiCustomCommandEvent;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasiplugin.utilities.Plugin;

public class Hosts extends TwasiCustomCommand {

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
            JsonObject object = new JsonParser().parse(Plugin.getApiContent("https://tmi.twitch.tv/hosts?include_logins=1&target=" + streamer.getTwitchId())).getAsJsonObject();
            JsonArray streamers = object.get("hosts").getAsJsonArray();
            if (streamers.size() == 0) {
                event.reply(getTranslation("twasi.utilities.hosts.nohoster", streamer.getDisplayName()));
                return;
            }
            String streamerUserNames = "";
            for (JsonElement elem : streamers) {
                streamerUserNames += ", " + elem.getAsJsonObject().get("host_display_name").getAsString();
            }
            event.reply(getTranslation("twasi.utilities.hosts.success", streamer.getDisplayName(), streamerUserNames.substring(2)));
        } catch (Exception e) {
            event.reply(getTranslation("twasi.utilities.hosts.failed"));
        }
    }

    @Override
    public boolean allowsTimer() {
        return true;
    }
}
