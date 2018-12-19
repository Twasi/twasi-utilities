package de.merlinw.twasi.utilities.commands;

import com.google.gson.JsonParser;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.models.Streamer;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;
import org.jetbrains.annotations.NotNull;

public abstract class BaseCommand {

    // static variables
    protected static JsonParser parser = new JsonParser();
    protected static String clientId = ServiceRegistry.get(ConfigService.class).getCatalog().twitch.clientId;
    protected static String clientSecret = ServiceRegistry.get(ConfigService.class).getCatalog().twitch.clientSecret;

    // User Class and Command Event
    protected TwasiUserPlugin plugin;
    protected TwasiCommandEvent event;

    // Twitch-Accounts
    protected TwitchAccount executor;
    protected Streamer streamer;

    // Command details
    protected String commandName;
    protected String command;

    // Constructor for setting variables
    public BaseCommand(@NotNull TwasiCommandEvent e, @NotNull TwasiUserPlugin plugin) {
        this.plugin = plugin;
        this.event = e;
        executor = e.getCommand().getSender();
        streamer = plugin.getTwasiInterface().getStreamer();
        commandName = e.getCommand().getCommandName();
        command = e.getCommand().getMessage();
    }

    // Void that finally executes the command
    public void executeCommand(){
        String commandOutput = getCommandOutput();
        if(commandOutput != null) reply(commandOutput);
    }

    // Force overridden void that returns the command answer
    protected abstract String getCommandOutput();

    // Void to reply to the command
    protected void reply(String text){
        this.event.getCommand().reply(text);
    }
}
