package ink.ptms.cronus.internal.task.listener;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.internal.task.block.TaskBlockBreak;
import ink.ptms.cronus.internal.task.block.TaskBlockPlace;
import me.skymc.taboolib.listener.TListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:24
 */
@TListener
public class ListenerBlock implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(BlockBreakEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskBlockBreak.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(BlockPlaceEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskBlockPlace.class);
    }
}
