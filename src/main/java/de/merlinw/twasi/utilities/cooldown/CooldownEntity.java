package de.merlinw.twasi.utilities.cooldown;

import de.merlinw.twasi.utilities.commands.BaseCommand;
import net.twasi.core.database.models.BaseEntity;
import net.twasi.core.database.models.User;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

import java.util.Calendar;

@Entity(value = "Twasi.Utilities.Cooldown", noClassnameStored = true)
public class CooldownEntity extends BaseEntity {

    private Class<? extends BaseCommand> commandClass;
    private String twitchId;
    private Calendar executionTime;

    @Reference
    private User user;

    public CooldownEntity() {
    }

    public CooldownEntity(Class<? extends BaseCommand> commandClass, String twitchId, User user) {
        this.commandClass = commandClass;
        this.twitchId = twitchId;
        this.user = user;
        this.executionTime = Calendar.getInstance();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTwitchId() {
        return twitchId;
    }

    public void setTwitchId(String twitchId) {
        this.twitchId = twitchId;
    }

    public Class<? extends BaseCommand> getCommandClass() {
        return commandClass;
    }

    public void setCommandClass(Class<? extends BaseCommand> commandClass) {
        this.commandClass = commandClass;
    }

    public Calendar getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Calendar executionTime) {
        this.executionTime = executionTime;
    }
}
