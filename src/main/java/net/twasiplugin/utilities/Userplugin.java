package net.twasiplugin.utilities;

import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import net.twasi.core.plugin.api.events.TwasiInstallEvent;
import net.twasiplugin.utilities.commands.BaseCommand;
import net.twasiplugin.utilities.commands.check.Check;
import net.twasiplugin.utilities.commands.game.Game;
import net.twasiplugin.utilities.commands.hosts.Hosts;
import net.twasiplugin.utilities.commands.title.Title;
import net.twasiplugin.utilities.commands.uptime.Uptime;
import net.twasiplugin.utilities.commands.wiki.Wiki;

import java.time.Duration;

public class Userplugin extends TwasiUserPlugin {

    @Override
    public void onInstall(TwasiInstallEvent e) {
        e.getAdminGroup().addKey("twasi.utilities.streamer.*");
        e.getModeratorsGroup().addKey("twasi.utilities.mod.*");
        e.getDefaultGroup().addKey("twasi.utilities.default.*");
    }

    @Override
    public void onUninstall(TwasiInstallEvent e) {
        e.getAdminGroup().removeKey("twasi.utilities.streamer.*");
        e.getModeratorsGroup().removeKey("twasi.utilities.mod.*");
        e.getDefaultGroup().removeKey("twasi.utilities.default.*");
    }

    @Override
    public void onCommand(TwasiCommandEvent e) {
        // Cooldown cooldown = new Cooldown(e.getCommand().getSender().getTwitchId(), getTwasiInterface().getStreamer().getUser(), getUserCooldown());

        BaseCommand command = null;
        switch (e.getCommand().getCommandName().toLowerCase()) {
            case "check":
                command = new Check(e, this);
                break;
            case "wiki":
                command = new Wiki(e, this);
                break;
            case "game":
                command = new Game(e, this);
                break;
            case "title":
            case "status":
                command = new Title(e, this);
                break;
            case "uptime":
                command = new Uptime(e, this);
                break;
            case "hosts":
                command = new Hosts(e, this);
                break;
        }

        if (command != null) command.executeCommand();
    }

    private long getUserCooldown() {
        Duration cooldownDuration = Duration.ofMinutes(3);
        return cooldownDuration.toMillis();
    }
}
