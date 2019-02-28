package net.twasiplugin.utilities.commands.uptime;

import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.plugin.api.TwasiCustomCommand;
import net.twasi.core.plugin.api.TwasiCustomCommandEvent;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.twitchapi.helix.streams.response.StreamDTO;
import net.twasi.twitchapi.options.TwitchRequestOptions;

import java.time.Duration;
import java.util.Calendar;

import static java.time.Duration.*;
import static net.twasi.twitchapi.TwitchAPI.helix;

public class Uptime extends TwasiCustomCommand {

    public Uptime(TwasiUserPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getCommandName() {
        return "uptime";
    }

    @Override
    public void process(TwasiCustomCommandEvent event) {
        TwitchAccount twitchacc = event.getStreamer().getUser().getTwitchAccount();
        try {
            StreamDTO stream = helix().streams().getStreamsByUser(twitchacc.getTwitchId(), 1, new TwitchRequestOptions().withAuth(twitchacc.toAuthContext())).getData().get(0);
            Duration duration = ofMillis(Calendar.getInstance().getTimeInMillis() - stream.getStartedAt().getTime());
            event.reply(getTranslation("twasi.utilities.uptime.onlinesince.sentence", twitchacc.getDisplayName(), formTimeUnitFromDuration(duration)));
        } catch (Exception e) {
            event.reply(getTranslation("twasi.utilities.uptime.notonline", twitchacc.getDisplayName()));
        }
    }

    public String formTimeUnitFromDuration(Duration duration) {
        long days = duration.toDays(), hours = duration.minus(ofDays(days)).toHours(), mins = duration.minus(ofHours(hours)).toMinutes(), secs = duration.minus(ofMinutes(mins)).getSeconds();
        String key = "twasi.utilities.units.", spacer = "twasi.utilities.uptime.onlinesince.spacer";
        if (days > 0) {
            return String.format(
                    "%d %s, %d %s %s %d %s", // "1 day, 9 hours and 45 minutes" f.e.
                    days,
                    getTranslation(key + ((days == 1) ? "day" : "days")),
                    hours,
                    getTranslation(key + ((hours == 1) ? "hour" : "hours")),
                    getTranslation(spacer),
                    mins,
                    getTranslation(key + ((mins == 1) ? "minute" : "minutes"))
            );
        }
        if (hours > 0) {
            return String.format(
                    "%d %s %s %d %s", // "3 hours and 34 minutes" f.e.
                    hours,
                    getTranslation(key + ((hours == 1) ? "hour" : "hours")),
                    getTranslation(spacer),
                    mins,
                    getTranslation(key + ((mins == 1) ? "minute" : "minutes"))
            );
        }
        if (mins > 0) {
            return String.format(
                    "%d %s %s %d %s", // "43 minutes and 23 seconds" f.e.
                    mins,
                    getTranslation(key + ((mins == 1) ? "minute" : "minutes")),
                    getTranslation(spacer),
                    secs,
                    getTranslation(key + ((secs == 1) ? "second" : "seconds"))
            );
        }
        return String.format("%d %s", secs, getTranslation(key + ((secs == 1) ? "second" : "seconds")));
    }

}
