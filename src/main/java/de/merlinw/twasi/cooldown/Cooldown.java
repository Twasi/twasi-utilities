package de.merlinw.twasi.cooldown;

import de.merlinw.twasi.commands.BaseCommand;
import net.twasi.core.database.models.User;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;

import java.util.Calendar;

public class Cooldown {

    private long millis;
    private String twitchId;
    private User user;
    private CooldownRepository repo = ServiceRegistry.get(DataService.class).get(CooldownRepository.class);
    private boolean hasSkipPermission = false;

    // Cooldown constructor
    public Cooldown(String twitchId, User user, long millis){
        this.user = user;
        this.twitchId = twitchId;
        this.millis = millis;
    }

    // True if command can be executed again, false if not
    public boolean executeCommand(Class<? extends BaseCommand> commandClass){
        if(hasSkipPermission) return true;
        CooldownEntity entity = repo.getCooldownEntity(user, twitchId, commandClass);
        if(entity == null){
            entity = new CooldownEntity(commandClass, twitchId, user);
            repo.commit(entity);
            return true;
        }
        Calendar execution = entity.getExecutionTime();
        execution.add(Calendar.MILLISECOND, (int) this.millis);
        return execution.getTimeInMillis() < Calendar.getInstance().getTimeInMillis();
    }

    public void setHasSkipPermission() {
        this.hasSkipPermission = true;
    }
}
