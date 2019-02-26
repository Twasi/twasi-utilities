package net.twasiplugin.utilities.commands.check;

import net.twasi.core.database.lib.Repository;
import net.twasi.core.database.models.User;
import net.twasi.core.models.Streamer;
import org.mongodb.morphia.query.Query;

import java.util.List;

public class CheckRepository extends Repository<CheckEntity> {

    public CheckEntity getCheckEntityByUserAndTwitchId(User user, String twitchId) {
        Query<CheckEntity> entities = store.createQuery(CheckEntity.class);
        List<CheckEntity> entityList = entities.field("user").equal(user).field("twitchId").equal(twitchId).asList();
        if (entityList.size() > 0) return entityList.get(0);
        else return null;
    }

    public CheckEntity getCheckEntityByStreamerAndTwitchId(Streamer streamer, String twitchId){
        return getCheckEntityByUserAndTwitchId(streamer.getUser(), twitchId);
    }



}
