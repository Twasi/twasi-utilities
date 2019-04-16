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

    private List<TwasiPluginCommand> commands = new ArrayList<>();
    private List<TwasiVariable> variables = new ArrayList<>();

    public Userplugin() {
        // Register commands
        commands.add(new Check(this));
        commands.add(new Game(this));
        commands.add(new Hosts(this));
        commands.add(new Title(this));
        commands.add(new Uptime(this));
        commands.add(new Wiki(this));

        // Register variables
        variables.add(new ReadAPI(this));
        variables.add(new Sender(this));
        variables.add(new User(this));
        variables.add(new Random(this)); // Random
        variables.add(new Args(this)); // argument
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

    @Override
    public List<TwasiPluginCommand> getCommands() {
        return commands;
    }

    @Override
    public List<TwasiVariable> getVariables() {
        return variables;
    }
}
