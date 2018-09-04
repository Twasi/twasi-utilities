package de.merlinw.twasi;

import de.merlinw.twasi.commands.BaseCommand;
import de.merlinw.twasi.commands.check.Check;
import de.merlinw.twasi.commands.game.Game;
import de.merlinw.twasi.commands.title.Title;
import de.merlinw.twasi.commands.wiki.Wiki;
import de.merlinw.twasi.cooldown.Cooldown;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import net.twasi.core.plugin.api.events.TwasiInstallEvent;

import java.time.Duration;

public class Userplugin extends TwasiUserPlugin {

    @Override
    public void onInstall(TwasiInstallEvent e) {
        e.getAdminGroup().addKey("twasi.utilities.streamer.*");
        e.getModeratorsGroup().addKey("twasi.utilities.mod.*");
        e.getDefaultGroup().addKey("twasi.utilities.default.*");
        super.onInstall(e);
    }

    @Override
    public void onUninstall(TwasiInstallEvent e) {
        e.getAdminGroup().removeKey("twasi.utilities.streamer.*");
        e.getModeratorsGroup().removeKey("twasi.utilities.mod.*");
        e.getDefaultGroup().removeKey("twasi.utilities.default.*");
        super.onUninstall(e);
    }

    @Override
    public void onCommand(TwasiCommandEvent e) {
        Cooldown cooldown = new Cooldown(e.getCommand().getSender().getTwitchId(), getTwasiInterface().getStreamer().getUser(), getUserCooldown());

        BaseCommand command = null;
        switch(e.getCommand().getCommandName().toLowerCase()){
            case "check":
                command = new Check(e,this);
                break;
            case "wiki":
                command = new Wiki(e, this);
                break;
            case "game":
                command = new Game(e, this);
                break;
            case "title":
                command = new Title(e, this);
                break;
        }

        /*if(command != null && cooldown.executeCommand(command.getClass()))*/ command.executeCommand();
    }

    private long getUserCooldown() {
        Duration cooldownDuration = Duration.ofMinutes(3);
        return cooldownDuration.toMillis();
    }
}
