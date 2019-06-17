package ink.ptms.cronus.internal.task.player.damage;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.special.Countable;
import ink.ptms.cronus.internal.bukkit.DamageCause;
import ink.ptms.cronus.internal.bukkit.Entity;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.Task;
import me.skymc.taboolib.damage.DamageUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_death")
public class TaskPlayerDeath extends Countable {

    private Entity attacker;
    private ItemStack weapon;
    private DamageCause cause;

    public TaskPlayerDeath(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        weapon = data.containsKey("weapon") ? BukkitParser.toItemStack(data.get("weapon")) : null;
        attacker = data.containsKey("attacker") ? BukkitParser.toEntity(data.get("attacker")) : null;
        cause = data.containsKey("cause") ? BukkitParser.toDamageCause(data.get("cause")) : null;
    }

    @Override
    public boolean isValid(Player player, DataQuest dataQuest, Event event) {
        if (player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            LivingEntity a = DamageUtils.getLivingAttackerInDamageEvent((EntityDamageByEntityEvent) player.getLastDamageCause());
            return (weapon == null || weapon.isItem(a.getEquipment().getItemInHand())) && (attacker == null || attacker.isSelect(a)) && (cause == null || cause.isSelect(player.getLastDamageCause().getCause()));
        } else {
            return weapon == null && attacker == null && (cause == null || cause.isSelect(player.getLastDamageCause().getCause()));
        }
    }

    @Override
    public String toString() {
        return "TaskPlayerDeath{" +
                "count=" + count +
                ", attacker=" + attacker +
                ", weapon=" + weapon +
                ", cause=" + cause +
                ", action=" + action +
                ", config=" + config +
                ", condition=" + condition +
                ", guide=" + guide +
                '}';
    }
}