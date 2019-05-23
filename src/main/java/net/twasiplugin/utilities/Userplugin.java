package net.twasiplugin.utilities;

import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.TwasiVariable;
import net.twasi.core.plugin.api.customcommands.TwasiPluginCommand;
import net.twasi.core.plugin.api.events.TwasiInstallEvent;
import net.twasiplugin.utilities.commands.check.Check;
import net.twasiplugin.utilities.commands.game.Game;
import net.twasiplugin.utilities.commands.hosts.Hosts;
import net.twasiplugin.utilities.commands.title.Title;
import net.twasiplugin.utilities.commands.uptime.Uptime;
import net.twasiplugin.utilities.commands.wiki.Wiki;
import net.twasiplugin.utilities.variables.args.Args;
import net.twasiplugin.utilities.variables.random.Random;
import net.twasiplugin.utilities.variables.readapi.ReadAPI;
import net.twasiplugin.utilities.variables.sender.Sender;
import net.twasiplugin.utilities.variables.user.User;

import java.util.ArrayList;
import java.util.List;

public class Userplugin extends TwasiUserPlugin {

    public Userplugin() {
        // Register commands
        registerCommand(Check.class);
        registerCommand(Game.class);
        registerCommand(Hosts.class);
        registerCommand(Title.class);
        registerCommand(Uptime.class);
        registerCommand(Wiki.class);

        // Register variables
        registerVariable(ReadAPI.class);
        registerVariable(Sender.class);
        registerVariable(User.class);
        registerVariable(Random.class);
        registerVariable(Args.class);
    }

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
}
