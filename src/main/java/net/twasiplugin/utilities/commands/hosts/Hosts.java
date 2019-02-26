package net.twasiplugin.utilities.commands.hosts;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import net.twasiplugin.utilities.Plugin;
import net.twasiplugin.utilities.commands.BaseCommand;
import org.jetbrains.annotations.NotNull;

public class Hosts extends BaseCommand {

    public Hosts(@NotNull TwasiCommandEvent e, @NotNull TwasiUserPlugin plugin) {
        super(e, plugin);
    }

    @Override
    protected String getCommandOutput() {
        try {
            TwitchAccount streamer = this.streamer.getUser().getTwitchAccount();
            JsonObject object = parser.parse(Plugin.getApiContent("https://tmi.twitch.tv/hosts?include_logins=1&target=" + streamer.getTwitchId())).getAsJsonObject();
            JsonArray streamers = object.get("hosts").getAsJsonArray();
            if (streamers.size() == 0)
                return plugin.getTranslation("twasi.utilities.hosts.nohoster", streamer.getDisplayName());
            String streamerUserNames = "";
            for (JsonElement elem : streamers) {
                streamerUserNames += ", " + elem.getAsJsonObject().get("host_display_name").getAsString();
            }
            return plugin.getTranslation("twasi.utilities.hosts.success", streamer.getDisplayName(), streamerUserNames.substring(2));
        } catch (Exception e) {
            return plugin.getTranslation("twasi.utilities.hosts.failed");
        }
    }

}
