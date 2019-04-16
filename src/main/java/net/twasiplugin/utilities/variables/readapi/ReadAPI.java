package net.twasiplugin.utilities.variables.readapi;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.models.Message.TwasiMessage;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.TwasiVariable;
import net.twasiplugin.utilities.Plugin;

import java.util.Arrays;
import java.util.List;

public class ReadAPI extends TwasiVariable {

    public ReadAPI(TwasiUserPlugin owner) {
        super(owner);
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("readapi", "urlfetch");
    }

    @Override
    public String process(String name, TwasiInterface inf, String[] params, TwasiMessage message) {
        if (params.length > 0) {
            String url = params[0];
            if (url.toLowerCase().startsWith("https://")) {
                try {
                    String content = Plugin.getApiContent(url);
                    if (content.length() > 350) {
                        // Too long answer
                        return "TOO_LONG_ANSWER";
                    } else {
                        return content;
                    }
                } catch (Exception e) {
                    // Invalid request
                    return "BAD_REQUEST";
                }
            } else {
                // Wrong protocol/invalid or insecure
                url = url.toLowerCase();
                if (url.startsWith("http://")) {
                    // Insecure
                    return "INSECURE(NO_HTTPS)";
                } else {
                    // Wrong protocol or invalid url
                    if (url.contains("://")) {
                        // Wrong protocol
                        return "WRONG_PROTOCOL";
                    } else {
                        // No protocol
                        return "INVALID_URL";
                    }
                }
            }
        } else {
            // No link
            return "NO_URL";
        }
    }
}
