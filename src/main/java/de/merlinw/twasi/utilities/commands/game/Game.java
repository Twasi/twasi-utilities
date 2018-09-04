package de.merlinw.twasi.utilities.commands.game;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.merlinw.twasi.utilities.Plugin;
import de.merlinw.twasi.utilities.commands.BaseCommand;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Game extends BaseCommand {
    private String game;

    public Game(@NotNull TwasiCommandEvent e, @NotNull TwasiUserPlugin plugin) {
        super(e, plugin);
        this.game = Plugin.getCommandArgs(this.command);
    }

    @Override
    protected String getCommandOutput() {
        if(!this.streamer.getUser().hasPermission(this.executor, "twasi.utilities.mod.game")) return null;

        if(this.game == null) return plugin.getTranslation("twasi.utilities.game.helptext");
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "OAuth " + twitchToken);
        header.put("Content-Type", "application/json");
        header.put("Accept", "application/vnd.twitchtv.v5+json");
        JsonObject object = new JsonObject(), insideObejct = new JsonObject();
        insideObejct.add("game", new JsonPrimitive(this.game));
        object.add("channel", insideObejct);
        try {
            Plugin.putApiContent("https://api.twitch.tv/kraken/channels/"
                    + streamer.getUser().getTwitchAccount().getTwitchId() + "?client_id=" + clientId
                    + "&client_secret=" + clientSecret, header, object.toString());
            return plugin.getTranslation("twasi.utilities.game.changed", this.game);
        } catch (IOException e) {
            e.printStackTrace();
            return plugin.getTranslation("twasi.utilities.game.requestfailed");
        }
    }
}
