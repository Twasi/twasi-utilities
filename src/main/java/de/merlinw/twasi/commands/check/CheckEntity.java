package de.merlinw.twasi.commands.check;

import net.twasi.core.database.models.BaseEntity;
import net.twasi.core.database.models.User;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

import java.util.Date;

@Entity(value = "Twasi.Utilities.check", noClassnameStored = true)
public class CheckEntity extends BaseEntity {

    @Reference
    private User user;

    private Date date;
    private String twitchId;

    public CheckEntity() {
    }

    public CheckEntity(User user, String twitchId, Date date) {
        this.user = user;
        this.date = date;
        this.twitchId = twitchId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
