package net.twasiplugin.utilities.commands.check;

import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.models.Streamer;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.customcommands.TwasiCustomCommandEvent;
import net.twasi.core.plugin.api.customcommands.TwasiPluginCommand;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.twitchapi.helix.users.Users;
import net.twasi.twitchapi.helix.users.response.UserDTO;
import net.twasi.twitchapi.helix.users.response.UserFollowDTO;
import net.twasi.twitchapi.options.TwitchRequestOptions;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static net.twasi.twitchapi.TwitchAPI.helix;

public class Check extends TwasiPluginCommand {

    private CheckRepository repo = ServiceRegistry.get(DataService.class).get(CheckRepository.class);

    public Check(TwasiUserPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getCommandName() {
        return "check";
    }

    @Override
    public void process(TwasiCustomCommandEvent event) {
        String userToCheck = null;
        Streamer streamer = event.getStreamer();
        TwitchAccount executor = event.getSender();
        CheckMode checkMode = CheckMode.SELF;
        if (event.getArgs().size() > 0) {
            userToCheck = event.getArgs().get(0);
            checkMode = CheckMode.OTHER;
        }
        if (executor.getTwitchId().equals(streamer.getUser().getTwitchAccount().getTwitchId()) && checkMode == CheckMode.SELF ||
                userToCheck != null && userToCheck.equalsIgnoreCase(executor.getUserName())) {
            event.reply(getTranslation("twasi.utilities.check.selfcheck", executor.getDisplayName()));
            return;
        }

        String followerUsername = executor.getDisplayName();
        CheckEntity entity;
        if (checkMode == CheckMode.SELF)
            entity = repo.getCheckEntityByStreamerAndTwitchId(streamer, executor.getTwitchId());
        else entity = null;

        Date date;
        if (entity == null) {

            Users.UsersWithAuth request = helix().users().withAuth(streamer.getUser().getTwitchAccount().toAuthContext());

            UserFollowDTO dto;
            UserDTO userDto;
            if (checkMode == CheckMode.SELF) dto = request.getFollowedBy(executor.getTwitchId());
            else {
                List<UserDTO> user = helix().users().getUsers(null, new String[]{userToCheck}, new TwitchRequestOptions().withAuth(streamer.getUser().getTwitchAccount().toAuthContext()));
                if (user != null && user.size() > 0) {
                    userDto = user.get(0);
                    dto = request.getFollowedBy(userDto.getId());
                    followerUsername = userDto.getDisplayName();
                } else {
                    event.reply(getTranslation("twasi.utilities.check.usernotfound", userToCheck));
                    return;
                }
            }

            if (dto != null) {
                date = dto.getFollowedAt();
                CheckEntity checkEntity = new CheckEntity(streamer, dto.getFromId(), date);
                repo.add(checkEntity);
                repo.commitAll();
            } else {
                event.reply(getTranslation(
                        "twasi.utilities.check.nofollow." + checkMode.toString(),
                        followerUsername,
                        streamer.getUser().getTwitchAccount().getDisplayName()
                ));
                return;
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

        String timeSinceFollow = number + " " + getTranslation("twasi.utilities.units." + unit);


        event.reply(getTranslation(
                "twasi.utilities.check.following." + checkMode.toString(),
                followerUsername,
                streamer.getUser().getTwitchAccount().getDisplayName(),
                new SimpleDateFormat(getTranslation("twasi.utilities.units.dateformat")).format(date),
                timeSinceFollow
        ));
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
