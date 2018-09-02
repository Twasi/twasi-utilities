package de.merlinw.twasi.cooldown;

import de.merlinw.twasi.commands.BaseCommand;
import net.twasi.core.database.lib.Repository;
import net.twasi.core.database.models.User;
import org.mongodb.morphia.query.Query;

import java.util.List;

public class CooldownRepository extends Repository<CooldownEntity> {

    public CooldownEntity getCooldownEntity(User user, String twitchId, Class<? extends BaseCommand> command) {
        Query<CooldownEntity> entities = store.createQuery(CooldownEntity.class);
        List<CooldownEntity> entityList = entities.field("user").equal(user).field("twitchId").equal(twitchId).field("commandClass").equal(command).asList();
        if (entityList.size() > 0) return entityList.get(0);
        else return null;
    }

}
