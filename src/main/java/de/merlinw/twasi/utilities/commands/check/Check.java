package de.merlinw.twasi.utilities.commands.check;

import de.merlinw.twasi.utilities.commands.BaseCommand;
import net.twasi.core.database.models.User;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.twitchapi.helix.users.response.UserFollowDTO;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

import static net.twasi.twitchapi.TwitchAPI.helix;

public class Check extends BaseCommand {
    private CheckRepository repo = ServiceRegistry.get(DataService.class).get(CheckRepository.class);

    public Check(@NotNull TwasiCommandEvent e, @NotNull TwasiUserPlugin plugin) {
        super(e, plugin);
    }

    @Override
    public String getCommandOutput() {
        if(this.executor.getTwitchId().equals(this.streamer.getUser().getTwitchAccount().getTwitchId()))
            return plugin.getTranslation("twasi.utilities.check.selfcheck", executor.getDisplayName());

        User user = plugin.getTwasiInterface().getStreamer().getUser();
        CheckEntity entity = repo.getCheckEntityByUser(user, executor.getTwitchId());

        Date date;
        if (entity == null) {
            UserFollowDTO dto = helix().users().withAuth(user.getTwitchAccount().toAuthContext()).getFollowedBy(executor.getTwitchId());
            if (dto != null) {
                date = dto.getFollowedAt();
                CheckEntity checkEntity = new CheckEntity(user, executor.getTwitchId(), date);
                repo.add(checkEntity);
                repo.commitAll();
            } else return plugin.getTranslation("twasi.utilities.check.nofollow", streamer.getUser().getTwitchAccount().getDisplayName());
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
