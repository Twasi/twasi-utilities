package net.twasiplugin.utilities.api;

import net.twasi.core.database.models.User;
import net.twasi.core.graphql.TwasiCustomResolver;
import net.twasiplugin.utilities.api.models.UtilitiesDTO;

public class UtilitiesResolver extends TwasiCustomResolver {

    public UtilitiesDTO getUtilities(String token) {
        User user = getUser(token);
        if (user == null) return null;
        return new UtilitiesDTO(user);
    }

}
