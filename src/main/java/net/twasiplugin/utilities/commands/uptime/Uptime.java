package net.twasiplugin.utilities.commands.uptime;

import net.twasiplugin.utilities.commands.BaseCommand;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import net.twasi.twitchapi.helix.streams.response.StreamDTO;
import net.twasi.twitchapi.options.TwitchRequestOptions;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Calendar;

import static net.twasi.twitchapi.TwitchAPI.helix;

public class Uptime extends BaseCommand {

    public Uptime(@NotNull TwasiCommandEvent e, @NotNull TwasiUserPlugin plugin) {
        super(e, plugin);
    }

    @Override
    protected String getCommandOutput() {
        TwitchAccount twitchacc = streamer.getUser().getTwitchAccount();
        try {
            StreamDTO stream = helix().streams().getStreamsByUser(twitchacc.getTwitchId(), 1, new TwitchRequestOptions().withAuth(twitchacc.toAuthContext())).getData().get(0);
            Duration duration = Duration.ofMillis(Calendar.getInstance().getTimeInMillis() - stream.getStartedAt().getTime());
            return plugin.getTranslation("twasi.utilities.uptime.onlinesince", twitchacc.getDisplayName(), formTimeUnitFromDuration(duration));
        } catch (Exception e){
            return plugin.getTranslation("twasi.utilities.uptime.notonline", twitchacc.getDisplayName());
        }
    }

    public String formTimeUnitFromDuration(Duration duration) {
        long amount = duration.getSeconds();
        String unit = "second";
        if (amount > 60) {
            amount /= 60;
            unit = "minute";
        }
        if (amount > 60) {
            amount /= 60;
            unit = "hour";
        }
        if (amount != 1) unit += "s";
        return amount + " " + plugin.getTranslation("twasi.utilities.units." + unit);
    }

}
