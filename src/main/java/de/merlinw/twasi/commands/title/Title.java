package de.merlinw.twasi.commands.title;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.merlinw.twasi.Utilities;
import de.merlinw.twasi.commands.BaseCommand;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Title extends BaseCommand {
    private String title;

    public Title(@NotNull TwasiCommandEvent e, @NotNull TwasiUserPlugin plugin){
        super(e, plugin);
        this.title = Utilities.getCommandArgs(this.command);
    }

    @Override
    protected String getCommandOutput() {
        if(!this.streamer.getUser().hasPermission(this.executor, "twasi.utilities.mod.title")) return null;

        if(this.title == null) return plugin.getTranslation("twasi.utilities.title.helptext");
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "OAuth " + twitchToken);
        header.put("Content-Type", "application/json");
        header.put("Accept", "application/vnd.twitchtv.v5+json");
        JsonObject object = new JsonObject(), insideObejct = new JsonObject();
        insideObejct.add("status", new JsonPrimitive(this.title));
        object.add("channel", insideObejct);
        try {
            JsonObject obj = parser.parse(Utilities.putApiContent("https://api.twitch.tv/kraken/channels/"
                    + streamer.getUser().getTwitchAccount().getTwitchId() + "?client_id=" + clientId
                    + "&client_secret=" + clientSecret, header, object.toString())).getAsJsonObject();
            System.out.println(obj.toString());
            if(obj.has("status") && obj.get("status").getAsString().equals(this.title)) return plugin.getTranslation("twasi.utilities.title.changed", this.title);
            else throw new Exception("Title could not be changed.");
        } catch (Exception e) {
            e.printStackTrace();
            return plugin.getTranslation("twasi.utilities.title.requestfailed");
        }
    }
}
