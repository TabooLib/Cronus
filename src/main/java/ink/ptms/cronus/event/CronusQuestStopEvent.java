package ink.ptms.cronus.event;

import ink.ptms.cronus.internal.Quest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CronusQuestStopEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private Quest quest;

    public CronusQuestStopEvent(Player who, Quest quest) {
        super(who);
        this.quest = quest;
    }

    public static CronusQuestStopEvent call(Player who, Quest quest) {
        CronusQuestStopEvent event = new CronusQuestStopEvent(who, quest);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public Quest getQuest() {
        return quest;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
