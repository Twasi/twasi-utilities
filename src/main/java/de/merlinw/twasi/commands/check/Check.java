package de.merlinw.twasi.commands.check;

import com.google.gson.JsonObject;
import de.merlinw.twasi.Utilities;
import de.merlinw.twasi.commands.BaseCommand;
import net.twasi.core.database.models.User;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

public class Check extends BaseCommand {
    private CheckRepository repo = ServiceRegistry.get(DataService.class).get(CheckRepository.class);

    public Check(@NotNull TwasiCommandEvent e, @NotNull TwasiUserPlugin plugin) {
        super(e, plugin);
    }

    @Override
    public String getCommandOutput() {
        User user = plugin.getTwasiInterface().getStreamer().getUser();
        CheckEntity entity = repo.getCheckEntityByUser(user, executor.getTwitchId());

        Date date;
        if (entity == null) {
            try {
                JsonObject object = parser.parse(
                        Utilities.getApiContent("https://api.twitch.tv/kraken/users/" + executor.getUserName() + "/follows/channels/" + streamer.getUser().getTwitchAccount().getUserName() + "?client_id=" + clientId)
                ).getAsJsonObject();
                if (object.has("created_at")) {
                    date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(object.get("created_at").getAsString());
                    CheckEntity checkEntity = new CheckEntity(user, executor.getTwitchId(), date);
                    repo.add(checkEntity);
                    repo.commitAll();
                } else return plugin.getTranslation("twasi.utilities.check.nofollow", streamer.getUser().getTwitchAccount().getDisplayName());
            } catch (IOException | ParseException e1) {
                return plugin.getTranslation("twasi.utilities.check.requestfailed");
            }
        } else date = entity.getDate();
        Calendar current = Calendar.getInstance(), follow = Calendar.getInstance();
        follow.setTime(date);
        Duration followDuration = Duration.ofMillis(current.getTimeInMillis() - follow.getTimeInMillis());
        long seconds = followDuration.getSeconds();

        int number;
        String unit;

        if(seconds < (60 * 60 * 24)){
            number = (int) (seconds / 60 / 60);
            unit = number == 1 ? "hour" : "hours";
        } else if(seconds < (60 * 60 * 24 * 30)) {
            number = (int) (seconds / 60 / 60 / 24);
            unit = number == 1 ? "day" : "days";
        } else if(seconds < (60 * 60 * 24 * 30 * 12)){
            number = (int) (seconds / 60 / 60 / 24 / 30);
            unit = number == 1 ? "month" : "months";
        } else {
            number = (int) (seconds / 60 / 60 / 24 / 30 / 12);
            unit = number == 1 ? "year" : "years";
        }

        String timeSinceFollow = number + " " + plugin.getTranslation("twasi.utilities.units." + unit);
        
        return plugin.getTranslation(
                "twasi.utilities.check.following",
                executor.getDisplayName(),
                streamer.getUser().getTwitchAccount().getDisplayName(),
                new SimpleDateFormat(plugin.getTranslation("twasi.utilities.units.dateformat")).format(date),
                timeSinceFollow
        );
    }
}
