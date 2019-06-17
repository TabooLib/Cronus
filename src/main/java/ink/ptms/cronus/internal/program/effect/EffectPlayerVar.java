package ink.ptms.cronus.internal.program.effect;

import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 15:24
 */
@Auto
public class EffectPlayerVar extends Effect {

    private String name;
    private String symbol;
    private String value;

    @Override
    public String pattern() {
        return "player\\.var\\.(?<name>\\S+) (?<symbol>\\S+)( (?<value>.+))?";
    }

    @Override
    public void match(Matcher matcher) {
        name = matcher.group("name");
        value = matcher.group("value");
        symbol = matcher.group("symbol");
    }

    @Override
    public void eval(Program program) {
        if (program instanceof QuestProgram) {
            ink.ptms.cronus.uranus.program.effect.EffectVal.eval(program, value, symbol, name, ((QuestProgram) program).getDataPlayer().getDataTemp());
        }
    }

    @Override
    public String toString() {
        return "EffectPlayerVar{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
