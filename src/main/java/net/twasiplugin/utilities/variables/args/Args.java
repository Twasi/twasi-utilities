package net.twasiplugin.utilities.variables.args;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.models.Message.TwasiCommand;
import net.twasi.core.models.Message.TwasiMessage;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.TwasiVariable;
import net.twasi.core.plugin.api.customcommands.TwasiCustomCommandEvent;

import java.util.Collections;
import java.util.List;

public class Args extends TwasiVariable {

    public Args(TwasiUserPlugin owner) {
        super(owner);
    }

    @Override
    public List<String> getNames() {
        return Collections.singletonList("args");
    }

    @Override
    public String process(String name, TwasiInterface inf, String[] params, TwasiMessage message) {
        try {
            TwasiCustomCommandEvent fakeEvent = new TwasiCustomCommandEvent( // Create event to parse args easier
                    new TwasiCommand(message.getMessage(), message.getType(), message.getSender(), message.getTwasiInterface())
            );
            if (params.length > 0) {
                int argNum = Integer.parseInt(params[0]) - 1; // -1 because lists start at 0
                if (argNum < 0) argNum = 0;
                return fakeEvent.getArgs().get(argNum);
            } else {
                return fakeEvent.getArgsAsOne();
            }
        } catch (Exception e) {
            return "";
        }
    }
}
