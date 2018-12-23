package de.merlinw.twasi.utilities.commands;

import com.google.gson.JsonParser;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.models.Streamer;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public abstract class BaseCommand {

    // static variables
    protected static JsonParser parser = new JsonParser();

    // User Class and Command Event
    protected TwasiUserPlugin plugin;
    protected TwasiCommandEvent event;

    // Twitch-Accounts
    protected TwitchAccount executor;
    protected Streamer streamer;

    // Command details
    protected String commandName;
    protected String command;
    protected String commandArgs;

    // Constructor for setting variables
    public BaseCommand(@NotNull TwasiCommandEvent e, @NotNull TwasiUserPlugin plugin) {
        this.plugin = plugin;
        this.event = e;
        executor = e.getCommand().getSender();
        streamer = plugin.getTwasiInterface().getStreamer();
        commandName = e.getCommand().getCommandName();
        command = e.getCommand().getMessage();
        commandArgs = getCommandArgs(command);
    }

    // Void that finally executes the command
    public void executeCommand() {
        String commandOutput = getCommandOutput();
        if (commandOutput != null) reply(commandOutput);
    }

    // Force overridden void that returns the command answer
    protected abstract String getCommandOutput();

    // Void to reply to the command
    protected void reply(String text) {
        this.event.getCommand().reply(text);
    }

    // Function to get command args as list
    protected List<String> getCommandArgsAsList() {
        if (this.commandArgs == null || this.commandArgs.equals("")) return null;
        return Arrays.asList(this.commandArgs.split(" "));
    }

    // Function to get specific command arg
    protected String getCommandArg(int num) {
        try {
            return getCommandArgsAsList().get(num);
        } catch (Exception e){
            return null;
        }
    }

    // Extract command args from message
    private static String getCommandArgs(String command) {
        String rt = "";
        String[] arr = command.split(" ");
        if (arr.length > 1) {
            String[] copArr = new String[arr.length - 1];
            System.arraycopy(arr, 1, copArr, 0, arr.length - 1);
            for (String s : copArr) if (s != null && !s.equals("")) rt += s + " ";
            return rt.substring(0, rt.length() - 1);
        } else return null;
    }
}
