package ink.ptms.cronus.internal;

import com.google.common.collect.Maps;
import com.ilummc.tlib.bungee.chat.ComponentSerializer;
import com.ilummc.tlib.resources.TLocale;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.function.FunctionParser;
import ink.ptms.cronus.util.Utils;
import me.skymc.taboolib.bookformatter.BookFormatter;
import me.skymc.taboolib.bookformatter.builder.BookBuilder;
import me.skymc.taboolib.json.tellraw.TellrawJson;
import me.skymc.taboolib.string.VariableFormatter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author 坏黑
 * @Since 2019-06-09 17:54
 */
public class QuestBook {

    private ConfigurationSection config;
    private String id;
    private List<String> head;
    private List<String> format;
    private Map<String, Integer> list = Maps.newHashMap();

    public QuestBook(ConfigurationSection config) {
        this.id = config.getName();
        this.config = config;
        this.head = TLocale.Translate.setColored(config.getStringList("head"));
        this.format = TLocale.Translate.setColored(config.getStringList("format"));
        ConfigurationSection list = config.getConfigurationSection("list");
        if (list != null) {
            list.getKeys(false).forEach(keyword -> this.list.put(keyword, list.getInt(keyword)));
        }
    }

    public void open(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(Cronus.getInst(), () -> BookFormatter.openPlayer(player, toBuilder(player).build()));
    }

    public BookBuilder toBuilder(Player player) {
        DataPlayer playerData = CronusAPI.getData(player);
        BookBuilder bookBuilder = BookFormatter.writtenBook();
        AtomicInteger line = new AtomicInteger();
        TellrawJson[] json = {TellrawJson.create()};
        for (String h : head) {
            json[0].append(h).newLine();
            line.getAndIncrement();
        }
        playerData.getQuest().values().stream()
                .filter(f -> {
                    Quest quest = f.getQuest();
                    return quest != null && quest.getKeyword().stream().anyMatch(list::containsKey);
                })
                .sorted((b, a) -> Integer.compare(a.getQuest().getKeyword().stream().filter(list::containsKey).map(list::get).findFirst().orElse(0), b.getQuest().getKeyword().stream().filter(list::containsKey).map(list::get).findFirst().orElse(0)))
                .forEach(q -> {
                    try {
                        QuestStage questStage = q.getStage();
                        QuestProgram program = new QuestProgram(player, q);
                        for (String f : format) {
                            if (f.contains("{content}")) {
                                for (String c : questStage.getContentGlobal()) {
                                    if (line.incrementAndGet() == 15) {
                                        bookBuilder.addPages(ComponentSerializer.parse(json[0].toRawMessage(player)));
                                        json[0] = TellrawJson.create();
                                    }
                                    appendLine(json[0], program, c);
                                }
                            } else {
                                if (line.incrementAndGet() == 15) {
                                    bookBuilder.addPages(ComponentSerializer.parse(json[0].toRawMessage(player)));
                                    json[0] = TellrawJson.create();
                                }
                                appendLine(json[0], program, f
                                        .replace("{id}", q.getCurrentQuest())
                                        .replace("{display}", q.getQuest().getDisplay()));
                            }
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                });
        bookBuilder.addPages(ComponentSerializer.parse(json[0].toRawMessage(player)));
        return bookBuilder;
    }

    public static void appendLine(TellrawJson json, QuestProgram program, String line) {
        for (VariableFormatter.Variable variable : new VariableFormatter(FunctionParser.parseAll(program, line)).find().getVariableList()) {
            if (variable.isVariable()) {
                if (variable.getText().startsWith("hover:")) {
                    String[] text = variable.getText().substring("hover:".length()).split("\\|");
                    json.append(text[0]);
                    if (text.length > 1) {
                        json.hoverText(text[1]);
                    }
                    if (text.length > 2) {
                        json.clickCommand("/" + text[2]);
                    }
                } else if (variable.getText().startsWith("hover-item:")) {
                    String[] text = variable.getText().substring("hover-item:".length()).split("\\|");
                    json.append(text[0]);
                    if (text.length > 1) {
                        json.hoverItem(Utils.NonNull(Cronus.getCronusService().getItemStorage().getItem(text[1])));
                    }
                    if (text.length > 2) {
                        json.clickCommand("/" + text[2]);
                    }
                } else {
                    json.append("<" + variable.getText() + ">");
                }
            } else {
                json.append(variable.getText());
            }
        }
        json.newLine();
    }

    public ConfigurationSection getConfig() {
        return config;
    }

    public String getId() {
        return id;
    }

    public List<String> getHead() {
        return head;
    }

    public List<String> getFormat() {
        return format;
    }

    public Map<String, Integer> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "QuestBook{" +
                "config=" + config +
                ", id='" + id + '\'' +
                ", head=" + head +
                ", format=" + format +
                ", list=" + list +
                '}';
    }
}