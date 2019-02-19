package de.merlinw.twasi.utilities.commands.check;

import de.merlinw.twasi.utilities.commands.BaseCommand;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.twitchapi.helix.users.Users;
import net.twasi.twitchapi.helix.users.response.UserDTO;
import net.twasi.twitchapi.helix.users.response.UserFollowDTO;
import net.twasi.twitchapi.options.TwitchRequestOptions;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static net.twasi.twitchapi.TwitchAPI.helix;

public class Check extends BaseCommand {

    private CheckRepository repo = ServiceRegistry.get(DataService.class).get(CheckRepository.class);

    private CheckMode checkMode = CheckMode.SELF;
    private String userToCheck;

    public Check(@NotNull TwasiCommandEvent e, @NotNull TwasiUserPlugin plugin) {
        super(e, plugin);
        this.userToCheck = getCommandArg(0);
        if (this.userToCheck != null) this.checkMode = CheckMode.OTHER;
    }

    @Override
    public String getCommandOutput() {
        if (this.executor.getTwitchId().equals(this.streamer.getUser().getTwitchAccount().getTwitchId()) && this.checkMode == CheckMode.SELF ||
                this.userToCheck != null && this.userToCheck.equalsIgnoreCase(this.executor.getUserName()))
            return plugin.getTranslation("twasi.utilities.check.selfcheck", executor.getDisplayName());

        String followerUsername = executor.getDisplayName();
        CheckEntity entity;
        if (this.checkMode == CheckMode.SELF)
            entity = repo.getCheckEntityByStreamerAndTwitchId(streamer, executor.getTwitchId());
        else entity = null;

        Date date;
        if (entity == null) {

            Users.UsersWithAuth request = helix().users().withAuth(streamer.getUser().getTwitchAccount().toAuthContext());

            UserFollowDTO dto;
            UserDTO userDto;
            if (this.checkMode == CheckMode.SELF) dto = request.getFollowedBy(executor.getTwitchId());
            else {
                List<UserDTO> user = helix().users().getUsers(null, new String[]{this.userToCheck}, new TwitchRequestOptions().withAuth(streamer.getUser().getTwitchAccount().toAuthContext()));
                if (user != null && user.size() > 0) {
                    userDto = user.get(0);
                    dto = request.getFollowedBy(userDto.getId());
                    followerUsername = userDto.getDisplayName();
                } else return plugin.getTranslation("twasi.utilities.check.usernotfound", this.userToCheck);
            }

            if (dto != null) {
                date = dto.getFollowedAt();
                CheckEntity checkEntity = new CheckEntity(streamer, dto.getFromId(), date);
                repo.add(checkEntity);
                repo.commitAll();
            } else {
                return plugin.getTranslation(
                        "twasi.utilities.check.nofollow." + this.checkMode.toString(),
                        followerUsername,
                        streamer.getUser().getTwitchAccount().getDisplayName()
                );
            }
        } else date = entity.getDate();

        Calendar current = Calendar.getInstance(), follow = Calendar.getInstance();
        follow.setTime(date);
        Duration followDuration = Duration.ofMillis(current.getTimeInMillis() - follow.getTimeInMillis());
        long seconds = followDuration.getSeconds();

        int number;
        String unit;

        if (seconds < (60 * 60 * 24)) {
            number = (int) (seconds / 60 / 60);
            unit = number == 1 ? "hour" : "hours";
        } else if (seconds < (60 * 60 * 24 * 30)) {
            number = (int) (seconds / 60 / 60 / 24);
            unit = number == 1 ? "day" : "days";
        } else if (seconds < (60 * 60 * 24 * 30 * 12)) {
            number = (int) (seconds / 60 / 60 / 24 / 30);
            unit = number == 1 ? "month" : "months";
        } else {
            number = (int) (seconds / 60 / 60 / 24 / 30 / 12);
            unit = number == 1 ? "year" : "years";
        }

        String timeSinceFollow = number + " " + plugin.getTranslation("twasi.utilities.units." + unit);


        return plugin.getTranslation(
                "twasi.utilities.check.following." + this.checkMode.toString(),
                followerUsername,
                streamer.getUser().getTwitchAccount().getDisplayName(),
                new SimpleDateFormat(plugin.getTranslation("twasi.utilities.units.dateformat")).format(date),
                timeSinceFollow
        );
    }

    enum CheckMode {
        SELF {
            @Override
            public String toString() {
                return "self";
            }
        },
        OTHER {
            @Override
            public String toString() {
                return "other";
            }
        }
    }
}
