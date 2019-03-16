package net.twasiplugin.utilities.variables.readapi;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.models.Message.TwasiMessage;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.TwasiVariable;

import java.util.Collections;
import java.util.List;

public class ReadAPI extends TwasiVariable {

    public ReadAPI(TwasiUserPlugin owner) {
        super(owner);
    }

    @Override
    public List<String> getNames() {
        return Collections.singletonList("readapi");
    }

    @Override
    public String process(String name, TwasiInterface inf, String[] params, TwasiMessage message) {
        return null;
    }
}
