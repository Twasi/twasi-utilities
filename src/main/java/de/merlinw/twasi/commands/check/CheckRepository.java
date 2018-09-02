package de.merlinw.twasi.commands.check;

import net.twasi.core.database.lib.Repository;
import net.twasi.core.database.models.User;
import org.mongodb.morphia.query.Query;

import java.util.List;

public class CheckRepository extends Repository<CheckEntity> {

    public CheckEntity getCheckEntityByUser(User user, String twitchId) {
        Query<CheckEntity> entities = store.createQuery(CheckEntity.class);
        List<CheckEntity> entityList = entities.field("user").equal(user).field("twitchId").equal(twitchId).asList();
        if (entityList.size() > 0) return entityList.get(0);
        else return null;
    }

}
